package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.mvi.android.BaseMviViewModel
import com.blackcatz.android.hnews.ui.stories.domain.StoryRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

typealias StoriesBaseViewModel = BaseMviViewModel<StoriesIntent, StoriesViewState, StoriesAction>

@HiltViewModel
class StoriesViewModel @Inject constructor(
    private val storiesActionProcessorHolder: StoriesActionProcessorHolder
) : StoriesBaseViewModel() {

    override fun compose(): Observable<StoriesViewState> {
        return intentsSubject
            .map(this::actionFromIntents)
            .compose(storiesActionProcessorHolder.actionProcessor)
            .scan(StoriesViewState.empty, reducer)
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

    private val reducer =
        BiFunction<StoriesViewState, StoriesResult, StoriesViewState> { prev: StoriesViewState, result: StoriesResult ->
            when (result) {
                StoriesResult.LoadStoriesResult.Loading -> {
                    prev.copy(isLoading = true)
                }

                is StoriesResult.LoadStoriesResult.Success -> {
                    val items = prev.itemList + result.storyResponse.stories
                    val nextPage = result.storyResponse.page + 1
                    prev.copy(
                        itemList = items,
                        nextPage = nextPage,
                        isLoading = false
                    )
                }

                is StoriesResult.LoadStoriesResult.Error -> {
                    prev.copy(error = result.throwable, isLoading = false)
                }
            }
        }
}