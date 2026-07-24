package com.blackcatz.android.hnews.data

import com.blackcatz.android.hnews.data.local.RemoteKeysEntity
import com.blackcatz.android.hnews.data.local.StoryEntity
import com.blackcatz.android.hnews.data.network.response.ItemResponse

fun itemResponse(
    id: Int = 1,
    title: String? = "title",
    by: String = "author",
    score: Int? = 10,
    kids: List<Int>? = null,
    url: String? = "https://example.com",
    type: String = "story",
    time: Long = 0L,
): ItemResponse = ItemResponse(
    id = id,
    type = type,
    by = by,
    time = time,
    kids = kids,
    url = url,
    score = score,
    title = title,
)

fun storyEntity(
    id: Long = 1L,
    position: Int = 0,
    title: String = "title",
    author: String = "author",
    score: Int = 10,
    totalComment: Int = 0,
    url: String = "https://example.com",
): StoryEntity = StoryEntity(
    id = id,
    position = position,
    title = title,
    author = author,
    score = score,
    totalComment = totalComment,
    url = url,
)

fun remoteKeysEntity(
    storyId: Long = 1L,
    prevKey: Int? = null,
    nextKey: Int? = null,
): RemoteKeysEntity = RemoteKeysEntity(
    storyId = storyId,
    prevKey = prevKey,
    nextKey = nextKey,
)
