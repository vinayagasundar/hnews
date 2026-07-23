package com.blackcatz.android.hnews.data.mapper

import com.blackcatz.android.hnews.data.local.StoryEntity
import com.blackcatz.android.hnews.data.model.Story
import com.blackcatz.android.hnews.data.network.response.ItemResponse
import java.net.URL

fun ItemResponse.toStoryEntity(position: Int): StoryEntity {
    return StoryEntity(
        id = id.toLong(),
        position = position,
        title = title.orEmpty(),
        author = by,
        score = score ?: 0,
        totalComment = kids.orEmpty().size,
        url = url.orEmpty()
    )
}

fun StoryEntity.toStory(): Story {
    val domain =
        url.takeIf { it.isNotBlank() }.let { runCatching { URL(it).host }.getOrNull() }.orEmpty()
    return Story(
        id = id,
        title = title,
        author = author,
        noOfVotes = score,
        totalComment = totalComment,
        domain = domain,
    )
}
