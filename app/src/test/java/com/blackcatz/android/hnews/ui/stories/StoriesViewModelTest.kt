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
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class StoriesViewModelTest {

    private val rxLifeCycle: RxLifeCycle = TestRxLifeCycle()
    private val itemRepo: ItemRepo = mock()
    private val storiesActionProcessorHolder = StoriesActionProcessorHolder(itemRepo, rxLifeCycle)
    private lateinit var viewModel: StoriesViewModel

    @Before
    fun setUp() {
        viewModel = StoriesViewModel(storiesActionProcessorHolder, rxLifeCycle)
    }

    @Test
    fun `should Return story for when InitialIntent is called`() {
        whenever(
            itemRepo.getStories(
                0,
                DEFAULT_ITEM_SIZE,
                Story.ASK,
                true
            )
        ).thenReturn(Observable.just(MockItem.allItems))
        val intentPublisher = PublishSubject.create<StoriesIntent>()
        viewModel.processIntents(intentPublisher.hide())
        val testObserver = viewModel.states()
            .test()
        intentPublisher.onNext(StoriesIntent.RefreshIntent(Story.ASK))
        testObserver
            .assertValueAt(0) {
                it == StoriesViewState(false, emptyList(), null, 0)
            }
            .assertValueAt(1) {
                it == StoriesViewState(false, MockItem.allItems, null, 1)
            }
            .dispose()
    }


    @Test
    fun `should return All Story when RefreshIntent is called`() {
        whenever(
            itemRepo.getStories(
                0,
                DEFAULT_ITEM_SIZE,
                Story.ASK,
                true
            )
        ).thenReturn(Observable.just(MockItem.allItems))
        val intentPublisher = PublishSubject.create<StoriesIntent>()
        viewModel.processIntents(intentPublisher.hide())
        val testObserver = viewModel.states()
            .test()
        intentPublisher.onNext(StoriesIntent.RefreshIntent(Story.ASK))
        testObserver
            .assertValueAt(0) {
                it == StoriesViewState(false, emptyList(), null, 0)
            }
            .assertValueAt(1) {
                it == StoriesViewState(false, MockItem.allItems, null, 1)
            }
            .dispose()
    }

    @Test
    fun `should return next list of stories when LoadMoreIntent called`() {
        whenever(itemRepo.getStories(0, 1, Story.ASK, true))
            .thenReturn(Observable.just(listOf(MockItem.itemOne)))
        whenever(itemRepo.getStories(1, 1, Story.ASK, true))
            .thenReturn(Observable.just(listOf(MockItem.itemTwo)))

        val intentPublisher = PublishSubject.create<StoriesIntent>()
        viewModel.processIntents(intentPublisher.hide())
        val testObserver = viewModel.states()
            .test()

        intentPublisher.onNext(StoriesIntent.LoadStories(StoryRequest(0, Story.ASK, 1)))

        testObserver.assertValueAt(1) {
            it == StoriesViewState(false, listOf(MockItem.itemOne), null, 1)
        }

        intentPublisher.onNext(StoriesIntent.LoadStories(StoryRequest(1, Story.ASK, 1)))

        testObserver.assertValueAt(2) {
            it == StoriesViewState(false, listOf(MockItem.itemOne, MockItem.itemTwo), null, 2)
        }.dispose()
    }

}