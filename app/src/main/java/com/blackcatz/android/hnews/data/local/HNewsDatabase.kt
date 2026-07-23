package com.blackcatz.android.hnews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StoryEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class HNewsDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
