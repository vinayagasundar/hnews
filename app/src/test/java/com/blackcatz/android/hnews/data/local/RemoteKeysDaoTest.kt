package com.blackcatz.android.hnews.data.local

import androidx.test.core.app.ApplicationProvider
import androidx.room.Room
import com.blackcatz.android.hnews.data.remoteKeysEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RemoteKeysDaoTest {

    private lateinit var db: HNewsDatabase
    private lateinit var dao: RemoteKeysDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HNewsDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = db.remoteKeysDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `remoteKeysStoryId returns inserted row`() = runTest {
        dao.insertAll(listOf(remoteKeysEntity(storyId = 1L, nextKey = 20)))

        val result = dao.remoteKeysStoryId(1L)

        assertEquals(20, result?.nextKey)
    }

    @Test
    fun `remoteKeysStoryId returns null for unknown id`() = runTest {
        val result = dao.remoteKeysStoryId(999L)
        assertNull(result)
    }

    @Test
    fun `insertAll with conflicting storyId replaces nextKey`() = runTest {
        dao.insertAll(listOf(remoteKeysEntity(storyId = 1L, nextKey = 20)))
        dao.insertAll(listOf(remoteKeysEntity(storyId = 1L, nextKey = 40)))

        val result = dao.remoteKeysStoryId(1L)

        assertEquals(40, result?.nextKey)
    }

    @Test
    fun `clearRemoteKeys removes all rows`() = runTest {
        dao.insertAll(listOf(remoteKeysEntity(storyId = 1L), remoteKeysEntity(storyId = 2L)))

        dao.clearRemoteKeys()

        assertNull(dao.remoteKeysStoryId(1L))
        assertNull(dao.remoteKeysStoryId(2L))
    }
}
