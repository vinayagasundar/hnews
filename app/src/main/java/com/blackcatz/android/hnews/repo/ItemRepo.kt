package com.blackcatz.android.hnews.repo

import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvi.rx.SchedulerProvider
import com.blackcatz.android.hnews.network.HackerAPI
import io.reactivex.Observable

interface ItemRepo {
    fun getStories(story: Story): Observable<List<Item>>

    fun getStories(page: Int = 0, size: Int, story: Story): Observable<List<Item>>
}


class ItemRepoImpl(
    private val hackerAPI: HackerAPI,
    private val schedulerProvider: SchedulerProvider
) : ItemRepo {
    override fun getStories(story: Story): Observable<List<Item>> {
        return Observable.just(story)
            .flatMap {
                getItemIds(it)
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .flatMapIterable { it }
            .take(20)
            .flatMap(this::getItem)
            .toList()
            .toObservable()
    }



    override fun getStories(page: Int, size: Int, story: Story): Observable<List<Item>> {
        return Observable.just(story)
            .flatMap {
                getItemIds(it)
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .flatMapIterable { it }
            .skip((page * size).toLong())
            .map { it }
            .take(size.toLong())
            .flatMap(this::getItem)
            .toList()
            .toObservable()
    }

    private fun getItem(itemId: Long): Observable<Item> {
        return hackerAPI.getItem("$itemId")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .toObservable()
    }


    private fun getItemIds(it: Story): Observable<List<Long>> {
        return when (it) {
            Story.TOP -> hackerAPI.getTopStories().toObservable()
            Story.ASK -> hackerAPI.getAskStories().toObservable()
            Story.SHOW -> hackerAPI.getShowStories().toObservable()
            Story.JOB -> hackerAPI.getJobStories().toObservable()
            else -> hackerAPI.getTopStories().toObservable()
        }
    }

}


