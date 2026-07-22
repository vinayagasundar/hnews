package com.blackcatz.android.hnews.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey val storyId: Long,
    val prevKey: Int?,
    val nextKey: Int?,
)
