package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.mvi.MviIntent

sealed class StoriesIntent : MviIntent {
    object InitialIntent : StoriesIntent()
    data class RefreshIntent(val forcedUpdate: Boolean) : StoriesIntent()
}