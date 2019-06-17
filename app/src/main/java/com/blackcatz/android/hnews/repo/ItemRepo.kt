package com.blackcatz.android.hnews.repo

import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.network.HackerAPI
import io.reactivex.Observable

interface ItemRepo {
    fun getStories(page: Int = 0, size: Int, story: Story, forceUpdate: Boolean = true): Observable<List<Item>>
}


class ItemRepoImpl(
    private val hackerAPI: HackerAPI
) : ItemRepo {

    override fun getStories(page: Int, size: Int, story: Story, forceUpdate: Boolean): Observable<List<Item>> {
        return Observable.just(story)
            .flatMap { mapToHackerAPI(it) }
            .flatMapIterable { it }
            .skip((page * size).toLong())
            .map { it }
            .take(size.toLong())
            .flatMap(this::getItem)
            .toList()
            .toObservable()
    }

    private fun mapToHackerAPI(it: Story): Observable<List<Long>> {
        return when (it) {
            Story.TOP -> hackerAPI.getTopStories().toObservable()
            Story.ASK -> hackerAPI.getAskStories().toObservable()
            Story.SHOW -> hackerAPI.getShowStories().toObservable()
            Story.JOB -> hackerAPI.getJobStories().toObservable()
            else -> hackerAPI.getTopStories().toObservable()
        }
    }

    private fun getItem(itemId: Long): Observable<Item> {
        return hackerAPI.getItem("$itemId")
            .toObservable()
    }
}


