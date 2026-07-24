package com.blackcatz.android.hnews.data.local

import androidx.paging.PagingSource
import androidx.test.core.app.ApplicationProvider
import androidx.room.Room
import com.blackcatz.android.hnews.data.storyEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StoryDaoTest {

    private lateinit var db: HNewsDatabase
    private lateinit var dao: StoryDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HNewsDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = db.storyDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `pagingSource returns rows ordered by position then id`() = runTest {
        dao.insertAll(
            listOf(
                storyEntity(id = 2L, position = 0),
                storyEntity(id = 1L, position = 0),
                storyEntity(id = 3L, position = 1),
            )
        )

        val page = loadFirstPage()

        assertEquals(listOf(1L, 2L, 3L), page.data.map { it.id })
    }

    @Test
    fun `insertAll with conflicting id replaces existing row`() = runTest {
        dao.insertAll(listOf(storyEntity(id = 1L, title = "old")))
        dao.insertAll(listOf(storyEntity(id = 1L, title = "new")))

        val page = loadFirstPage()

        assertEquals(1, page.data.size)
        assertEquals("new", page.data.single().title)
    }

    @Test
    fun `clearAll removes all rows`() = runTest {
        dao.insertAll(listOf(storyEntity(id = 1L), storyEntity(id = 2L)))

        dao.clearAll()

        val page = loadFirstPage()
        assertTrue(page.data.isEmpty())
    }

    private suspend fun loadFirstPage(): PagingSource.LoadResult.Page<Int, StoryEntity> {
        val result = dao.pagingSource().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )
        return result as PagingSource.LoadResult.Page<Int, StoryEntity>
    }
}
