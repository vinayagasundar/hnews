package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.mvi.MviResult


sealed class StoriesResult : MviResult {
    sealed class LoadStoriesResult : StoriesResult() {
        data class Success(val stories: List<Item>) : LoadStoriesResult()
        data class Error(val throwable: Throwable) : LoadStoriesResult()
        object Loading : LoadStoriesResult()
    }
}