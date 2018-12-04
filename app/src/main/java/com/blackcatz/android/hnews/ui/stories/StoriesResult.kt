package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.mvi.MviResult
import com.blackcatz.android.hnews.ui.stories.domain.StoryResponse


sealed class StoriesResult : MviResult {
    sealed class LoadStoriesResult : StoriesResult() {
        data class Success(val stories: List<Item>) : LoadStoriesResult()
        data class Error(val throwable: Throwable) : LoadStoriesResult()
        object Loading : LoadStoriesResult()
    }


    sealed class LoadMoreStoriesResult : StoriesResult() {
        data class Success(val storyResponse: StoryResponse) : LoadMoreStoriesResult()
        data class Error(val throwable: Throwable) : LoadMoreStoriesResult()
        object Loading : LoadMoreStoriesResult()
    }
}