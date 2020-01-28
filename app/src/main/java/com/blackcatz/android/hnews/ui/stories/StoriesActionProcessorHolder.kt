package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.mvi.rx.RxLifeCycle
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.stories.StoriesAction.LoadStoriesAction
import com.blackcatz.android.hnews.ui.stories.StoriesResult.LoadStoriesResult
import com.blackcatz.android.hnews.ui.stories.domain.StoryResponse
import io.reactivex.ObservableTransformer

class StoriesActionProcessorHolder(
    private val itemRepo: ItemRepo,
    private val rxLifeCycle: RxLifeCycle
) {

    private val loadTaskProcessor =
        ObservableTransformer<LoadStoriesAction, LoadStoriesResult> { actions ->
            actions
                .flatMapSingle {
                    val request = it.storyRequest
                    itemRepo.getStories(
                        request.page,
                        request.size,
                        request.story,
                        request.forceUpdate
                    )
                        .compose(rxLifeCycle.async())
                        .map { stories ->
                            LoadStoriesResult.Success(
                                StoryResponse(
                                    request.page,
                                    stories
                                )
                            )
                        }
                        .cast(LoadStoriesResult::class.java)
                        .onErrorReturn { throwable -> LoadStoriesResult.Error(throwable) }
                }.startWith(LoadStoriesResult.Loading)
        }

    internal val actionProcessor = ObservableTransformer<StoriesAction, StoriesResult> { actions ->
        actions.ofType(LoadStoriesAction::class.java).compose(loadTaskProcessor)
    }
}