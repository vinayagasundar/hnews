package com.blackcatz.android.hnews.ui.topstories

import com.blackcatz.android.hnews.mvi.MviAction

sealed class TopStoriesAction: MviAction {
    data class LoadStoriesAction(val forceUpdate: Boolean): TopStoriesAction()
}