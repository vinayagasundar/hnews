package com.blackcatz.android.hnews.repo

import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvp.rx.MockSchedulerProvider
import com.blackcatz.android.hnews.network.HackerAPI
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test

class ItemRepoImplTest {

    private val hackerAPI: HackerAPI = mock()

    private val itemRepo: ItemRepo = ItemRepoImpl(hackerAPI, MockSchedulerProvider())

    private val listOfIds = listOf<Long>(100, 200)
    private val item = Item(
        id = 100,
        deleted = false,
        type = "type",
        by = "author",
        time = 0,
        text = "Hello World",
        dead = false,
        parent = 1,
        poll = 100,
        kids = null,
        url = null,
        score = 100,
        title = "Better World",
        parts = emptyList(),
        descendants = 1
    )

    @Test
    fun `should return Items for given Story`() {
        whenever(hackerAPI.getAskStories()).thenReturn(Single.just(listOfIds))
        whenever(hackerAPI.getItem(any())).thenReturn(Single.just(item))

        itemRepo.getStories(Story.ASK)
            .test()
            .assertValue {
                it[0] == item
            }
            .dispose()
    }
}