package com.blackcatz.android.hnews.ui.topstories

import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.topstories.TopStoriesAction.LoadStoriesAction
import com.blackcatz.android.hnews.ui.topstories.TopStoriesResult.LoadStoriesResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class TopStoriesActionProcessorHolder(private val itemRepo: ItemRepo) {

    private val loadTaskProcessor = ObservableTransformer<LoadStoriesAction, LoadStoriesResult> { actions ->
        actions.flatMap {
            itemRepo.getStories()
                .map { stories -> LoadStoriesResult.Success(stories) }
                .cast(LoadStoriesResult::class.java)
                .onErrorReturn { throwable -> LoadStoriesResult.Error(throwable) }
                .startWith(LoadStoriesResult.Loading)
        }
    }


    internal var actionProcessor = ObservableTransformer<TopStoriesAction, TopStoriesResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                shared.ofType(TopStoriesAction.LoadStoriesAction::class.java).compose(loadTaskProcessor).take(0),
                shared.ofType(TopStoriesAction.LoadStoriesAction::class.java).compose(loadTaskProcessor)
            ).cast(TopStoriesResult::class.java)
        }
    }
}