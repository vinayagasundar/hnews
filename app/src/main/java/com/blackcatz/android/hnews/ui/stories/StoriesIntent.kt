package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvi.MviIntent

sealed class StoriesIntent : MviIntent {
    data class InitialIntent(val story: Story) : StoriesIntent()
    data class RefreshIntent(val forcedUpdate: Boolean, val story: Story) : StoriesIntent()
}