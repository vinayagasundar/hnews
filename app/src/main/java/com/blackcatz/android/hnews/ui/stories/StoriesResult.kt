package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.mvi.MviResult
import com.blackcatz.android.hnews.ui.stories.domain.StoryResponse


sealed class StoriesResult : MviResult {
    sealed class LoadStoriesResult : StoriesResult() {
        object Loading : LoadStoriesResult()
        data class Success(val storyResponse: StoryResponse) : LoadStoriesResult()
        data class Error(val throwable: Throwable) : LoadStoriesResult()
    }
}