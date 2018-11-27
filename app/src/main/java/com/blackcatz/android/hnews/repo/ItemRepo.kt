package com.blackcatz.android.hnews.repo

import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.network.HackerAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface ItemRepo {
    fun getStories(story: Story): Observable<List<Item>>
}


class ItemRepoImpl(private val hackerAPI: HackerAPI) : ItemRepo {
    override fun getStories(story: Story): Observable<List<Item>> {
        return Observable.just(story)
            .flatMap {
                when (it) {
                    Story.TOP -> hackerAPI.getTopStories().toObservable()
                    Story.ASK -> hackerAPI.getAskStories().toObservable()
                    Story.SHOW -> hackerAPI.getShowStories().toObservable()
                    Story.JOB -> hackerAPI.getJobStories().toObservable()
                    else -> hackerAPI.getTopStories().toObservable()
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapIterable { it }
            .take(20)
            .flatMap(this::getItem)
            .toList()
            .toObservable()
    }


    private fun getItem(itemId: Long): Observable<Item> {
        return hackerAPI.getItem("$itemId")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
    }
}


