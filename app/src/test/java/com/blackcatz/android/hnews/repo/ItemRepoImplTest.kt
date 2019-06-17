package com.blackcatz.android.hnews.repo

import com.blackcatz.android.hnews.data.MockItem
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.network.HackerAPI
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Test

class ItemRepoImplTest {

    private val hackerAPI: HackerAPI = mock()

    private val itemRepo: ItemRepo = ItemRepoImpl(hackerAPI)

    private val listOfIds = listOf<Long>(100, 200)

    @Test
    fun `should return Items for given story, page and size`() {
        whenever(hackerAPI.getAskStories()).thenReturn(Single.just(listOfIds))
        whenever(hackerAPI.getItem(any())).thenReturn(Single.just(MockItem.itemOne))

        itemRepo.getStories(0, 1, Story.ASK)
            .test()
            .assertValue {
                it[0] == MockItem.itemOne
            }
            .dispose()

        whenever(hackerAPI.getItem(any())).thenReturn(Single.just(MockItem.itemTwo))

        itemRepo.getStories(1, 1, Story.ASK)
            .test()
            .assertValue {
                it[0] == MockItem.itemTwo
            }
            .dispose()
    }
}