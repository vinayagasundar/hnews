package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.mvi.rx.RxLifeCycle
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.stories.StoriesAction.LoadStoriesAction
import com.blackcatz.android.hnews.ui.stories.StoriesResult.LoadMoreStoriesResult
import com.blackcatz.android.hnews.ui.stories.StoriesResult.LoadStoriesResult
import com.blackcatz.android.hnews.ui.stories.domain.StoryResponse
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiPredicate

class StoriesActionProcessorHolder(private val itemRepo: ItemRepo, private val rxLifeCycle: RxLifeCycle) {

    private val loadTaskProcessor = ObservableTransformer<LoadStoriesAction, LoadStoriesResult> { actions ->
        actions
            .distinctUntilChanged(BiPredicate { old, new ->
                return@BiPredicate !(!old.forceUpdate || new.forceUpdate)
            })
            .flatMap {
                itemRepo.getStories(it.story)
                    .compose(rxLifeCycle.async())
                    .map { stories -> LoadStoriesResult.Success(stories) }
                    .cast(LoadStoriesResult::class.java)
                    .onErrorReturn { throwable -> LoadStoriesResult.Error(throwable) }
            }
    }

    private val loadMoreTaskProcessor =
        ObservableTransformer<StoriesAction.LoadMoreStoriesAction, LoadMoreStoriesResult> { actions ->
            actions
                .flatMap {
                    val request = it.storyRequest
                    itemRepo.getStories(request.page, request.size, request.story)
                        .compose(rxLifeCycle.async())
                        .map { stories -> LoadMoreStoriesResult.Success(StoryResponse(request.page, stories)) }
                        .cast(LoadMoreStoriesResult::class.java)
                        .onErrorReturn { throwable -> LoadMoreStoriesResult.Error(throwable) }
                }
        }


    internal var actionProcessor = ObservableTransformer<StoriesAction, StoriesResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(StoriesAction.LoadStoriesAction::class.java).compose(loadTaskProcessor),
                shared.ofType(StoriesAction.LoadMoreStoriesAction::class.java).compose(loadMoreTaskProcessor)
            ).cast(StoriesResult::class.java)
        }
    }
}