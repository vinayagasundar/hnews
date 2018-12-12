package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.data.MockItem
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.stories.domain.StoryRequest
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class StoriesViewModelTest {

    private val itemRepo: ItemRepo = mock()
    private val storiesActionProcessorHolder = StoriesActionProcessorHolder(itemRepo)
    private lateinit var viewModel: StoriesViewModel

    @Before
    fun setUp() {
        viewModel = StoriesViewModel(storiesActionProcessorHolder)
    }

    @Test
    fun `should Return story for when InitialIntent is called`() {
        whenever(itemRepo.getStories(any())).thenReturn(Observable.just(MockItem.allItems))
        val intentPublisher = PublishSubject.create<StoriesIntent>()
        viewModel.processIntents(intentPublisher.hide())
        val testObserver = viewModel.states()
            .test()
        intentPublisher.onNext(StoriesIntent.InitialIntent(Story.ASK))
        testObserver
            .assertValueAt(0) {
                it == StoriesViewState(false, emptyList(), null,  0)
            }
            .assertValueAt(1) {
                it == StoriesViewState(false, MockItem.allItems, null,  1)
            }
            .dispose()
    }


    @Test
    fun `should return All Story when RefreshIntent is called`() {
        whenever(itemRepo.getStories(any())).thenReturn(Observable.just(MockItem.allItems))
        val intentPublisher = PublishSubject.create<StoriesIntent>()
        viewModel.processIntents(intentPublisher.hide())
        val testObserver = viewModel.states()
            .test()
        intentPublisher.onNext(StoriesIntent.RefreshIntent(true, Story.ASK))
        testObserver
            .assertValueAt(0) {
                it == StoriesViewState(false, emptyList(), null,  0)
            }
            .assertValueAt(1) {
                it == StoriesViewState(false, MockItem.allItems, null,  1)
            }
            .dispose()
    }

    @Test
    fun `should return next list of stories when LoadMoreIntent called`() {
        whenever(itemRepo.getStories(any(), any(), any()))
            .thenReturn(Observable.just(listOf(MockItem.itemOne)), Observable.just(listOf(MockItem.itemTwo)))

        val intentPublisher = PublishSubject.create<StoriesIntent>()
        viewModel.processIntents(intentPublisher.hide())
        val testObserver = viewModel.states()
            .test()

        intentPublisher.onNext(StoriesIntent.LoadMoreIntent(StoryRequest(0, Story.ASK, 1)))

        testObserver.assertValueAt(1) {
            it == StoriesViewState(false, listOf(MockItem.itemOne), null, 1)
        }

        intentPublisher.onNext(StoriesIntent.LoadMoreIntent(StoryRequest(1, Story.ASK, 1)))

        testObserver.assertValueAt(2) {
            it == StoriesViewState(false, listOf(MockItem.itemOne, MockItem.itemTwo), null, 2)
        }.dispose()
    }

}