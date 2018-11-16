package com.blackcatz.android.hnews.repo

import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.network.HackerAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface StoriesRepo {
    fun getStories(): Observable<List<Item>>
}


class StoriesRepoImpl(private val hackerAPI: HackerAPI) : StoriesRepo {
    override fun getStories(): Observable<List<Item>> {
        return hackerAPI.getTopStories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
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


