package com.blackcatz.android.hnews.repo

import androidx.collection.ArrayMap
import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.network.HackerAPI
import io.reactivex.Single

interface ItemRepo {
    fun getStories(
        page: Int = 0,
        size: Int,
        story: Story,
        forceUpdate: Boolean = true
    ): Single<List<Item>>
}


internal class ItemRepoImpl(
    private val hackerAPI: HackerAPI
) : ItemRepo {

    private val inMemoryCache = ArrayMap<Story, List<Long>>()

    override fun getStories(
        page: Int,
        size: Int,
        story: Story,
        forceUpdate: Boolean
    ): Single<List<Item>> {
        val skipCount = page * size
        return getStoriesIdList(story, forceUpdate)
            .flatMap {
                if (it.size < skipCount) {
                    Single.error(IllegalStateException("No more data"))
                } else {
                    Single.just(it)
                }
            }
            .flattenAsObservable { it }
            .skip(skipCount.toLong())
            .take(size.toLong())
            .flatMapSingle(this::getItem)
            .toList()
    }

    private fun getStoriesIdList(story: Story, forceUpdate: Boolean): Single<List<Long>> {
        return if (!forceUpdate
            && inMemoryCache.containsKey(story)
            && inMemoryCache[story]?.isNotEmpty() == true
        ) {
            Single.just(inMemoryCache[story])
        } else {
            mapToHackerAPI(story).doOnSuccess { inMemoryCache[story] = it }
        }
    }

    private fun mapToHackerAPI(it: Story): Single<List<Long>> {
        return when (it) {
            Story.TOP -> hackerAPI.getTopStories()
            Story.ASK -> hackerAPI.getAskStories()
            Story.SHOW -> hackerAPI.getShowStories()
            Story.JOB -> hackerAPI.getJobStories()
            else -> hackerAPI.getTopStories()
        }
    }

    private fun getItem(itemId: Long): Single<Item> {
        return hackerAPI.getItem("$itemId")

    }
}


