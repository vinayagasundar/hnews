package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.data.MockItem
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvi.rx.RxLifeCycle
import com.blackcatz.android.hnews.mvi.rx.TestRxLifeCycle
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.stories.domain.DEFAULT_ITEM_SIZE
import com.blackcatz.android.hnews.ui.stories.domain.StoryRequest
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class StoriesViewModelTest {

    private lateinit var rxLifeCycle: RxLifeCycle
    private lateinit var itemRepo: ItemRepo
    private lateinit var storiesActionProcessorHolder: StoriesActionProcessorHolder
    private lateinit var viewModel: StoriesViewModel

    @Before
    fun setUp() {
        rxLifeCycle = TestRxLifeCycle()
        itemRepo = mock()
        storiesActionProcessorHolder = StoriesActionProcessorHolder(itemRepo, rxLifeCycle)
        viewModel = StoriesViewModel(storiesActionProcessorHolder, rxLifeCycle)
    }

    @Test
    fun `should Return story for when InitialIntent is called`() {
        // given
        whenever(
            itemRepo.getStories(
                0,
                DEFAULT_ITEM_SIZE,
                Story.ASK,
                true
            )
        ).thenReturn(Single.just(MockItem.allItems))
        val intentPublisher = PublishSubject.create<StoriesIntent>()
        viewModel.processIntents(intentPublisher.hide())
        val testObserver = viewModel.states()
            .test()

        // when
        intentPublisher.onNext(StoriesIntent.RefreshIntent(Story.ASK))

        // then
        val expectStoriesViewStateList = listOf(
            StoriesViewState(false, emptyList(), null),
            StoriesViewState(true, emptyList(), null),
            StoriesViewState(false, MockItem.allItems, null, 1)
        )
        testObserver
            .assertValueSequence(expectStoriesViewStateList)
            .assertNoErrors()
            .assertNotComplete()
            .dispose()
    }


    @Test
    fun `should return All Story when RefreshIntent is called`() {
        // given
        whenever(
            itemRepo.getStories(
                0,
                DEFAULT_ITEM_SIZE,
                Story.ASK,
                true
            )
        ).thenReturn(Single.just(MockItem.allItems))
        val intentPublisher = PublishSubject.create<StoriesIntent>()
        viewModel.processIntents(intentPublisher.hide())
        val testObserver = viewModel.states()
            .test()

        // when
        intentPublisher.onNext(StoriesIntent.RefreshIntent(Story.ASK))

        // then
        val expectStoriesViewStateList = listOf(
            StoriesViewState(false, emptyList(), null),
            StoriesViewState(true, emptyList(), null),
            StoriesViewState(false, MockItem.allItems, null, 1)
        )
        testObserver
            .assertValueSequence(expectStoriesViewStateList)
            .assertNoErrors()
            .assertNotComplete()
            .dispose()
    }

    @Test
    fun `should return next list of stories when LoadMoreIntent called`() {
        // given
        whenever(itemRepo.getStories(0, 1, Story.ASK, false))
            .thenReturn(Single.just(listOf(MockItem.itemOne)))
        whenever(itemRepo.getStories(1, 1, Story.ASK, false))
            .thenReturn(Single.just(listOf(MockItem.itemTwo)))

        val intentPublisher = PublishSubject.create<StoriesIntent>()
        viewModel.processIntents(intentPublisher.hide())
        val testObserver = viewModel.states()
            .test()

        // when
        intentPublisher.onNext(StoriesIntent.LoadStories(StoryRequest(0, Story.ASK, 1)))
        intentPublisher.onNext(StoriesIntent.LoadStories(StoryRequest(1, Story.ASK, 1)))

        // then
        val expectStoriesViewStateList = listOf(
            StoriesViewState(false, emptyList(), null),
            StoriesViewState(true, emptyList(), null),
            StoriesViewState(false, listOf(MockItem.itemOne), null, 1),
            StoriesViewState(false, listOf(MockItem.itemOne, MockItem.itemTwo), null, 2)
        )

        testObserver.assertValueSequence(expectStoriesViewStateList)
            .assertNoErrors()
            .assertNotComplete()
            .dispose()
    }

}