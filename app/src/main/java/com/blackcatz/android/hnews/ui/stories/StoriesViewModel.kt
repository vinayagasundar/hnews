package com.blackcatz.android.hnews.ui.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blackcatz.android.hnews.mvi.android.BaseMviViewModel
import com.blackcatz.android.hnews.mvi.rx.RxLifeCycle
import com.blackcatz.android.hnews.mvi.rx.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
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

    private val intentFilter: ObservableTransformer<StoriesIntent, StoriesIntent>
        get() = ObservableTransformer { shared ->
            Observable.merge(
                shared.ofType(StoriesIntent.InitialIntent::class.java).take(1),
                shared.notOfType(StoriesIntent.InitialIntent::class.java)
            )
        }

    override fun compose(): Observable<StoriesViewState> {
        return intentsSubject
            .compose(rxLifeCycle.async())
            .compose(intentFilter)
            .map(this::actionFromIntents)
            .compose(storiesActionProcessorHolder.actionProcessor)
            .scan(StoriesViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect()
    }

    override fun actionFromIntents(intents: StoriesIntent): StoriesAction {
        return when (intents) {
            is StoriesIntent.InitialIntent -> StoriesAction.LoadStoriesAction(intents.story, false)
            is StoriesIntent.RefreshIntent -> StoriesAction.LoadStoriesAction(intents.story, intents.forcedUpdate)
            is StoriesIntent.LoadMoreIntent -> StoriesAction.LoadMoreStoriesAction(intents.storyRequest)
        }
    }

    private val reducer = BiFunction { previousState: StoriesViewState, result: StoriesResult ->
        when (result) {
            is StoriesResult.LoadStoriesResult -> when (result) {
                is StoriesResult.LoadStoriesResult.Loading -> {
                    previousState.copy(isLoading = true)
                }
                is StoriesResult.LoadStoriesResult.Success -> {
                    previousState.copy(isLoading = false, itemList = result.stories, nextPage = 1)
                }

                is StoriesResult.LoadStoriesResult.Error -> {
                    previousState.copy(error = result.throwable, isLoading = false)
                }
            }

            is StoriesResult.LoadMoreStoriesResult -> when (result) {
                is StoriesResult.LoadMoreStoriesResult.Success -> {
                    val items = previousState.itemList + result.storyResponse.stories
                    previousState.copy(
                        itemList = items,
                        nextPage = result.storyResponse.page + 1,
                        isLoading = false
                    )
                }
                is StoriesResult.LoadMoreStoriesResult.Error -> {
                    previousState.copy(error = result.throwable, isLoading = false)
                }
                StoriesResult.LoadMoreStoriesResult.Loading -> {
                    previousState.copy(isLoading = true)
                }
            }
        }
    }
}