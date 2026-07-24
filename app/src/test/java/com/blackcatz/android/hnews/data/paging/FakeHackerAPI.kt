package com.blackcatz.android.hnews.data.paging

import com.blackcatz.android.hnews.data.network.HackerAPI
import com.blackcatz.android.hnews.data.network.response.ItemResponse
import java.io.IOException

class FakeHackerAPI(
    private val topStoryIds: List<Long> = emptyList(),
    private val items: Map<Long, ItemResponse> = emptyMap(),
    private val failingIds: Set<Long> = emptySet(),
    private val throwOnGetTopStories: Throwable? = null,
) : HackerAPI {

    var getTopStoriesCallCount = 0
        private set

    var getItemCallCount = 0
        private set

    override suspend fun getTopStories(): List<Long> {
        getTopStoriesCallCount++
        throwOnGetTopStories?.let { throw it }
        return topStoryIds
    }

    override suspend fun getItem(itemId: Long): ItemResponse {
        getItemCallCount++
        if (itemId in failingIds) throw IOException("boom: $itemId")
        return items[itemId] ?: throw IOException("missing: $itemId")
    }
}
