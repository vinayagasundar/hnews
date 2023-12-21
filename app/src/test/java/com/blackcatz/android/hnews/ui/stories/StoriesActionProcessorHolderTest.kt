package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.data.MockItem
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvp.rx.MockSchedulerProvider
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.stories.domain.DEFAULT_ITEM_SIZE
import com.blackcatz.android.hnews.ui.stories.domain.StoryRequest
import com.blackcatz.android.hnews.ui.stories.domain.StoryResponse
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class StoriesActionProcessorHolderTest {

    private val itemRepo: ItemRepo = mock()
    private val schedulerProvider = MockSchedulerProvider()
    private val actionProcessorHolder = StoriesActionProcessorHolder(itemRepo, schedulerProvider)

    private val allItems = MockItem.allItems

    @Test
    fun `should return LoadStoriesResult given LoadStoriesAction`() {
        // given
        whenever(itemRepo.getStories(0, DEFAULT_ITEM_SIZE, Story.ASK, false)).thenReturn(
            Single.just(
                allItems
            )
        )

        // when
        val testObserver =
            Observable.just(StoriesAction.LoadStoriesAction(StoryRequest(story = Story.ASK)))
                .compose(actionProcessorHolder.actionProcessor)
                .test()

        // then
        val expectedValues = listOf(
            StoriesResult.LoadStoriesResult.Loading,
            StoriesResult.LoadStoriesResult.Success(StoryResponse(0, allItems))
        )
        testObserver
            .assertValueSequence(expectedValues)
            .assertComplete()
            .assertNoErrors()
            .dispose()
    }
}