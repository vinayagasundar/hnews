package com.blackcatz.android.hnews.data.repository

import androidx.paging.testing.asSnapshot
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.blackcatz.android.hnews.data.itemResponse
import com.blackcatz.android.hnews.data.local.HNewsDatabase
import com.blackcatz.android.hnews.data.network.response.ItemResponse
import com.blackcatz.android.hnews.data.paging.FakeHackerAPI
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StoryRepositoryTest {

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
    fun `getStoriesStream loads first page of mapped stories in position order`() = runTest {
        val ids = (1L..25L).toList()
        val items: Map<Long, ItemResponse> = ids.associateWith { itemResponse(id = it.toInt()) }
        val api = FakeHackerAPI(topStoryIds = ids, items = items)
        val repository = StoryRepository(api, db, db.storyDao())

        val snapshot = repository.getStoriesStream().asSnapshot()

        assertEquals(20, snapshot.size)
        assertEquals(1L, snapshot.first().id)
        assertEquals((1L..20L).toList(), snapshot.map { it.id })
    }
}
