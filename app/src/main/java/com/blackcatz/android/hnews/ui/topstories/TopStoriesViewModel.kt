package com.blackcatz.android.hnews.ui.topstories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blackcatz.android.hnews.mvi.android.BaseMviViewModel
import com.blackcatz.android.hnews.mvi.rx.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

typealias TopStoriesBaseViewModel = BaseMviViewModel<TopStoriesIntent, TopStoriesViewState, TopStoriesAction>

class TopStoriesViewModel(private val actionProcessorHolder: TopStoriesActionProcessorHolder) :
    TopStoriesBaseViewModel() {


    class Factory(private val actionProcessorHolder: TopStoriesActionProcessorHolder) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TopStoriesViewModel(actionProcessorHolder) as T
        }
    }

    private val intentFilter: ObservableTransformer<TopStoriesIntent, TopStoriesIntent>
        get() = ObservableTransformer { shared ->
            Observable.merge(
                shared.ofType(TopStoriesIntent.InitialIntent::class.java).take(1),
                shared.notOfType(TopStoriesIntent.InitialIntent::class.java)
            )
        }

    override fun compose(): Observable<TopStoriesViewState> {
        return intentsSubject.compose(intentFilter)
            .map(this::actionFromIntents)
            .compose(actionProcessorHolder.actionProcessor)
            .scan(TopStoriesViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect()
    }

    override fun actionFromIntents(intents: TopStoriesIntent): TopStoriesAction {
        return when (intents) {
            is TopStoriesIntent.InitialIntent -> TopStoriesAction.LoadStoriesAction(true)
            is TopStoriesIntent.RefreshIntent -> TopStoriesAction.LoadStoriesAction(intents.forcedUpdate)
        }
    }

    private val reducer = BiFunction { previousState: TopStoriesViewState, result: TopStoriesResult ->
        when (result) {
            is TopStoriesResult.LoadStoriesResult -> when (result) {
                is TopStoriesResult.LoadStoriesResult.Loading -> {
                    previousState.copy(isLoading = true)
                }
                is TopStoriesResult.LoadStoriesResult.Success -> {
                    previousState.copy(isLoading = false, itemList = result.stories)
                }

                is TopStoriesResult.LoadStoriesResult.Error -> {
                    previousState.copy(error = result.throwable)
                }
            }
        }
    }
}