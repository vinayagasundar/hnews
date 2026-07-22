package com.blackcatz.android.hnews.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "stories", indices = [Index(value = ["position"])])
data class StoryEntity(
    @PrimaryKey val id: Long,
    val position: Int,
    val title: String,
    val author: String,
    val score: Int,
    val totalComment: Int,
    val domain: String,
)
