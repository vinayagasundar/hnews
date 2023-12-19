package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.mvi.MviViewState

data class StoriesViewState(
    val isLoading: Boolean,
    val itemList: List<Item>,
    val error: Throwable?,
    val nextPage: Int = 0
) : MviViewState {
    companion object {
        val empty = StoriesViewState(false, emptyList(), null)
    }
}