package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.mvi.MviAction

sealed class StoriesAction: MviAction {
    data class LoadStoriesAction(val forceUpdate: Boolean): StoriesAction()
}