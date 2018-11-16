package com.blackcatz.android.hnews.ui.topstories

import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.mvi.MviViewState

data class TopStoriesViewState(
    val isLoading: Boolean,
    val itemList: List<Item>,
    val error: Throwable?
) : MviViewState {
    companion object {
        fun idle() = TopStoriesViewState(false, emptyList(), null)
    }
}