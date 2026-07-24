package com.blackcatz.android.hnews.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.blackcatz.android.hnews.data.itemResponse
import com.blackcatz.android.hnews.data.local.HNewsDatabase
import com.blackcatz.android.hnews.data.local.StoryEntity
import com.blackcatz.android.hnews.data.network.response.ItemResponse
import com.blackcatz.android.hnews.data.remoteKeysEntity
import com.blackcatz.android.hnews.data.storyEntity
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
@RunWith(RobolectricTestRunner::class)
class StoryRemoteMediatorTest {

    private lateinit var db: HNewsDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HNewsDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `initialize launches initial refresh`() = runTest {
        val mediator = StoryRemoteMediator(FakeHackerAPI(), db)
        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, mediator.initialize())
    }

    @Test
    fun `REFRESH happy path inserts stories and remote keys`() = runTest {
        val ids = (1L..5L).toList()
        val api = FakeHackerAPI(topStoryIds = ids, items = fakeItemsFor(ids))
        val mediator = StoryRemoteMediator(api, db)

        val result = mediator.load(LoadType.REFRESH, pagingState(emptyList()))

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val stories = loadAllStories()
        assertEquals(listOf(1L, 2L, 3L, 4L, 5L), stories.map { it.id })
        assertEquals(listOf(0, 1, 2, 3, 4), stories.map { it.position })

        (1L..5L).forEach { id ->
            val key = db.remoteKeysDao().remoteKeysStoryId(id)
            assertNull(key?.nextKey)
        }
    }

    @Test
    fun `REFRESH clears stale data before inserting`() = runTest {
        db.storyDao().insertAll(listOf(storyEntity(id = 999L)))
        val ids = (1L..3L).toList()
        val api = FakeHackerAPI(topStoryIds = ids, items = fakeItemsFor(ids))
        val mediator = StoryRemoteMediator(api, db)

        mediator.load(LoadType.REFRESH, pagingState(emptyList()))

        val stories = loadAllStories()
        assertTrue(stories.none { it.id == 999L })
        assertEquals(listOf(1L, 2L, 3L), stories.map { it.id })
    }

    @Test
    fun `REFRESH always re-fetches ids ignoring cached topStoryIds`() = runTest {
        val ids = (1L..3L).toList()
        val api = FakeHackerAPI(topStoryIds = ids, items = fakeItemsFor(ids))
        val mediator = StoryRemoteMediator(api, db)

        // Priming APPEND call: no matching remote key, so it returns early, but it still
        // fetches+caches topStoryIds first since that happens before the loadType branch.
        mediator.load(LoadType.APPEND, pagingState(listOf(storyEntity(id = 42L))))
        assertEquals(1, api.getTopStoriesCallCount)

        mediator.load(LoadType.REFRESH, pagingState(emptyList()))

        assertEquals(2, api.getTopStoriesCallCount)
    }

    @Test
    fun `PREPEND always reports end of pagination without fetching items`() = runTest {
        val api = FakeHackerAPI(topStoryIds = listOf(1L, 2L, 3L))
        val mediator = StoryRemoteMediator(api, db)

        val result = mediator.load(LoadType.PREPEND, pagingState(listOf(storyEntity(id = 1L))))

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        // ids are fetched once (topStoryIds starts uncached) before the PREPEND branch
        // short-circuits, but no per-item fetches happen and nothing is written.
        assertEquals(1, api.getTopStoriesCallCount)
        assertEquals(0, api.getItemCallCount)
        assertTrue(loadAllStories().isEmpty())
    }

    @Test
    fun `APPEND with no remote key for last item reports end of pagination`() = runTest {
        val api = FakeHackerAPI(topStoryIds = listOf(1L, 2L, 3L))
        val mediator = StoryRemoteMediator(api, db)

        val result = mediator.load(LoadType.APPEND, pagingState(listOf(storyEntity(id = 42L))))

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `APPEND with null nextKey reports end of pagination`() = runTest {
        db.remoteKeysDao().insertAll(listOf(remoteKeysEntity(storyId = 42L, nextKey = null)))
        val api = FakeHackerAPI(topStoryIds = listOf(1L, 2L, 3L))
        val mediator = StoryRemoteMediator(api, db)

        val result = mediator.load(LoadType.APPEND, pagingState(listOf(storyEntity(id = 42L))))

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `APPEND with valid nextKey fetches next page at correct positions`() = runTest {
        db.remoteKeysDao().insertAll(listOf(remoteKeysEntity(storyId = 42L, nextKey = 5)))
        val ids = (1L..25L).toList()
        val api = FakeHackerAPI(topStoryIds = ids, items = fakeItemsFor(ids))
        val mediator = StoryRemoteMediator(api, db)

        val result = mediator.load(LoadType.APPEND, pagingState(listOf(storyEntity(id = 42L))))

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        val stories = loadAllStories()
        assertEquals(20, stories.size)
        assertEquals((5..24).toList(), stories.map { it.position })
        assertEquals((6L..25L).toList(), stories.map { it.id })
    }

    @Test
    fun `APPEND with pageStart past end of ids reports end of pagination without writes`() = runTest {
        db.remoteKeysDao().insertAll(listOf(remoteKeysEntity(storyId = 42L, nextKey = 30)))
        val ids = (1L..25L).toList()
        val api = FakeHackerAPI(topStoryIds = ids, items = fakeItemsFor(ids))
        val mediator = StoryRemoteMediator(api, db)

        val result = mediator.load(LoadType.APPEND, pagingState(listOf(storyEntity(id = 42L))))

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        assertTrue(loadAllStories().isEmpty())
    }

    @Test
    fun `partial item failures are tolerated and only successes are persisted`() = runTest {
        val ids = (1L..5L).toList()
        val api = FakeHackerAPI(topStoryIds = ids, items = fakeItemsFor(ids), failingIds = setOf(2L, 4L))
        val mediator = StoryRemoteMediator(api, db)

        val result = mediator.load(LoadType.REFRESH, pagingState(emptyList()))

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        val stories = loadAllStories()
        assertEquals(listOf(1L, 3L, 5L), stories.map { it.id })
        assertNull(db.remoteKeysDao().remoteKeysStoryId(2L))
        assertNull(db.remoteKeysDao().remoteKeysStoryId(4L))
    }

    @Test
    fun `all items failing for a non-empty page reports error and leaves db untouched`() = runTest {
        val ids = (1L..5L).toList()
        val api = FakeHackerAPI(topStoryIds = ids, items = fakeItemsFor(ids), failingIds = ids.toSet())
        val mediator = StoryRemoteMediator(api, db)

        val result = mediator.load(LoadType.REFRESH, pagingState(emptyList()))

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertTrue((result as RemoteMediator.MediatorResult.Error).throwable is IOException)
        assertTrue(loadAllStories().isEmpty())
    }

    @Test
    fun `getTopStories IOException reports error`() = runTest {
        val api = FakeHackerAPI(throwOnGetTopStories = IOException("network down"))
        val mediator = StoryRemoteMediator(api, db)

        val result = mediator.load(LoadType.REFRESH, pagingState(emptyList()))

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun `getTopStories HttpException reports error`() = runTest {
        val httpException = HttpException(Response.error<Any>(500, "".toResponseBody(null)))
        val api = FakeHackerAPI(throwOnGetTopStories = httpException)
        val mediator = StoryRemoteMediator(api, db)

        val result = mediator.load(LoadType.REFRESH, pagingState(emptyList()))

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun `pageEnd coerced to ids size reports end of pagination with null next key`() = runTest {
        db.remoteKeysDao().insertAll(listOf(remoteKeysEntity(storyId = 42L, nextKey = 20)))
        val ids = (1L..25L).toList()
        val api = FakeHackerAPI(topStoryIds = ids, items = fakeItemsFor(ids))
        val mediator = StoryRemoteMediator(api, db)

        mediator.load(LoadType.APPEND, pagingState(listOf(storyEntity(id = 42L))))

        val stories = loadAllStories()
        assertEquals(5, stories.size)
        assertEquals((20..24).toList(), stories.map { it.position })
        assertEquals((21L..25L).toList(), stories.map { it.id })
        (21L..25L).forEach { assertNull(db.remoteKeysDao().remoteKeysStoryId(it)?.nextKey) }
    }

    private fun fakeItemsFor(ids: List<Long>): Map<Long, ItemResponse> =
        ids.associateWith { itemResponse(id = it.toInt()) }

    private fun pagingState(
        lastPageData: List<StoryEntity>,
        pageSize: Int = 20,
    ): PagingState<Int, StoryEntity> = PagingState(
        pages = if (lastPageData.isEmpty()) {
            emptyList()
        } else {
            listOf(PagingSource.LoadResult.Page(data = lastPageData, prevKey = null, nextKey = null))
        },
        anchorPosition = null,
        config = PagingConfig(pageSize = pageSize),
        leadingPlaceholderCount = 0,
    )

    private suspend fun loadAllStories(): List<StoryEntity> {
        val result = db.storyDao().pagingSource().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 1000, placeholdersEnabled = false)
        )
        return (result as PagingSource.LoadResult.Page<Int, StoryEntity>).data
    }
}
