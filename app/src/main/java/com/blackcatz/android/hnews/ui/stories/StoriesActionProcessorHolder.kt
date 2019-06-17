package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.mvi.rx.RxLifeCycle
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.stories.StoriesAction.LoadStoriesAction
import com.blackcatz.android.hnews.ui.stories.StoriesResult.LoadStoriesResult
import com.blackcatz.android.hnews.ui.stories.domain.StoryResponse
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiPredicate

class StoriesActionProcessorHolder(private val itemRepo: ItemRepo, private val rxLifeCycle: RxLifeCycle) {

    private val loadTaskProcessor = ObservableTransformer<LoadStoriesAction, LoadStoriesResult> { actions ->
        actions.distinctUntilChanged(BiPredicate { old, new ->
            return@BiPredicate !(!old.storyRequest.forceUpdate || new.storyRequest.forceUpdate)
        }).flatMap {
            val request = it.storyRequest
            itemRepo.getStories(request.page, request.size, request.story)
                .compose(rxLifeCycle.async())
                .map { stories -> LoadStoriesResult.Success(StoryResponse(request.page, stories)) }
                .cast(LoadStoriesResult::class.java)
                .onErrorReturn { throwable -> LoadStoriesResult.Error(throwable) }
        }
    }

    internal val actionProcessor = ObservableTransformer<StoriesAction, StoriesResult> { actions ->
        actions.ofType(StoriesAction.LoadStoriesAction::class.java).compose(loadTaskProcessor)
    }
}