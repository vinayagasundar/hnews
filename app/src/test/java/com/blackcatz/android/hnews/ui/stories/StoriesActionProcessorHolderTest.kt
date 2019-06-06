package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.data.MockItem
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvi.rx.RxLifeCycle
import com.blackcatz.android.hnews.mvi.rx.TestRxLifeCycle
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.stories.domain.StoryRequest
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Test

class StoriesActionProcessorHolderTest {

    private val itemRepo: ItemRepo = mock()
    private val rxLifeCycle: RxLifeCycle = TestRxLifeCycle()
    private val actionProcessorHolder = StoriesActionProcessorHolder(itemRepo, rxLifeCycle)

    private val allItems = MockItem.allItems

    @Test
    fun `should return LoadStoriesResult given LoadStoriesAction`() {
        whenever(itemRepo.getStories(any())).thenReturn(Observable.just(allItems))
        Observable.just(StoriesAction.LoadStoriesAction(Story.ASK, false))
            .compose(actionProcessorHolder.actionProcessor)
            .test()
            .assertValue {
                val loadStoriesResult = it as StoriesResult.LoadStoriesResult.Success
                loadStoriesResult.stories == allItems
            }
            .dispose()
    }


    @Test
    fun `should return LoadMoreStoriesResult given LoadMoreStoriesAction`() {
        whenever(itemRepo.getStories(any(), any(), any())).thenReturn(Observable.just(allItems))
        Observable.just(StoriesAction.LoadMoreStoriesAction(StoryRequest(0, Story.ASK)))
            .compose(actionProcessorHolder.actionProcessor)
            .test()
            .assertValue {
                val loadStoriesResult = it as StoriesResult.LoadMoreStoriesResult.Success
                loadStoriesResult.storyResponse.stories == allItems
            }
            .dispose()
    }
}