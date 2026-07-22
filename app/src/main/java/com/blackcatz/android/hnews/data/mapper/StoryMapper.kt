package com.blackcatz.android.hnews.data.mapper

import com.blackcatz.android.hnews.data.local.StoryEntity
import com.blackcatz.android.hnews.data.model.Story
import com.blackcatz.android.hnews.data.network.response.ItemResponse
import java.net.URL

fun ItemResponse.toStoryEntity(position: Int): StoryEntity {
    val domain = url?.let { runCatching { URL(it).host }.getOrNull() }.orEmpty()
    return StoryEntity(
        id = id.toLong(),
        position = position,
        title = title.orEmpty(),
        author = by,
        score = score ?: 0,
        totalComment = kids.orEmpty().size,
        domain = domain,
    )
}

fun StoryEntity.toStory(): Story = Story(
    id = id,
    title = title,
    author = author,
    noOfVotes = score,
    totalComment = position,
    domain = domain,
)
