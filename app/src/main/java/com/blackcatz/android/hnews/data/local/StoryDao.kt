package com.blackcatz.android.hnews.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {

    @Query("SELECT * FROM stories ORDER BY position ASC, id ASC")
    fun pagingSource(): PagingSource<Int, StoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<StoryEntity>)

    @Query("DELETE FROM stories")
    suspend fun clearAll()
}
