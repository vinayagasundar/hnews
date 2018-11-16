package com.blackcatz.android.hnews.ui.topstories

import com.blackcatz.android.hnews.mvi.MviIntent

sealed class TopStoriesIntent : MviIntent {
    object InitialIntent : TopStoriesIntent()
    data class RefreshIntent(val forcedUpdate: Boolean) : TopStoriesIntent()
}