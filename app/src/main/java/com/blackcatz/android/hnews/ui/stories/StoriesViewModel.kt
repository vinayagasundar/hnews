package com.blackcatz.android.hnews.ui.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blackcatz.android.hnews.mvi.android.BaseMviViewModel
import com.blackcatz.android.hnews.mvi.rx.RxLifeCycle
import com.blackcatz.android.hnews.ui.stories.domain.StoryRequest
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

typealias StoriesBaseViewModel = BaseMviViewModel<StoriesIntent, StoriesViewState, StoriesAction>

class StoriesViewModel(
    private val storiesActionProcessorHolder: StoriesActionProcessorHolder,
    private val rxLifeCycle: RxLifeCycle
) : StoriesBaseViewModel() {

    class Factory(
        private val storiesActionProcessorHolder: StoriesActionProcessorHolder,
        private val rxLifeCycle: RxLifeCycle
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return StoriesViewModel(storiesActionProcessorHolder, rxLifeCycle) as T
        }
    }

    override fun compose(): Observable<StoriesViewState> {
        return intentsSubject
            .compose(rxLifeCycle.async())
            .map(this::actionFromIntents)
            .compose(storiesActionProcessorHolder.actionProcessor)
            .scan(StoriesViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect()
    }

    override fun actionFromIntents(intents: StoriesIntent): StoriesAction {
        return when (intents) {
            is StoriesIntent.RefreshIntent -> StoriesAction.LoadStoriesAction(
                StoryRequest(
                    story = intents.story,
                    forceUpdate = true
                )
            )
            is StoriesIntent.LoadStories -> StoriesAction.LoadStoriesAction(intents.storyRequest)
        }
    }

    private val reducer = BiFunction { previousState: StoriesViewState, result: StoriesResult ->
        when (result) {
            is StoriesResult.LoadStoriesResult -> when (result) {
                is StoriesResult.LoadStoriesResult.Loading -> {
                    previousState.copy(isLoading = true)
                }
                is StoriesResult.LoadStoriesResult.Success -> {
                    val items = previousState.itemList + result.storyResponse.stories
                    val nextPage = result.storyResponse.page + 1
                    previousState.copy(
                        itemList = items,
                        nextPage = nextPage,
                        isLoading = false
                    )
                }

                is StoriesResult.LoadStoriesResult.Error -> {
                    previousState.copy(error = result.throwable, isLoading = false)
                }
            }
        }
    }
}