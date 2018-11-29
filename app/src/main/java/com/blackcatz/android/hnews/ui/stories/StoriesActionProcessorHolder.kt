package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.stories.StoriesAction.LoadStoriesAction
import com.blackcatz.android.hnews.ui.stories.StoriesResult.LoadStoriesResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiPredicate

class StoriesActionProcessorHolder(private val itemRepo: ItemRepo) {

    private val loadTaskProcessor = ObservableTransformer<LoadStoriesAction, LoadStoriesResult> { actions ->
        actions
            .distinctUntilChanged(BiPredicate { old, new ->
                return@BiPredicate !(!old.forceUpdate || new.forceUpdate)
            })
            .flatMap {
                itemRepo.getStories(it.story)
                    .map { stories -> LoadStoriesResult.Success(stories) }
                    .cast(LoadStoriesResult::class.java)
                    .onErrorReturn { throwable -> LoadStoriesResult.Error(throwable) }
                    .startWith(LoadStoriesResult.Loading)
            }
    }


    internal var actionProcessor = ObservableTransformer<StoriesAction, StoriesResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(StoriesAction.LoadStoriesAction::class.java).compose(loadTaskProcessor).take(0),
                shared.ofType(StoriesAction.LoadStoriesAction::class.java).compose(loadTaskProcessor)
            ).cast(StoriesResult::class.java)
        }
    }
}