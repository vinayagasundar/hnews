package com.blackcatz.android.hnews.ui.topstories

import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.mvi.MviResult


sealed class TopStoriesResult : MviResult {
    sealed class LoadStoriesResult : TopStoriesResult() {
        data class Success(val stories: List<Item>) : LoadStoriesResult()
        data class Error(val throwable: Throwable) : LoadStoriesResult()
        object Loading : LoadStoriesResult()
    }
}