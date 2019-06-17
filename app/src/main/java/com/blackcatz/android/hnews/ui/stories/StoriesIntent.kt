package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvi.MviIntent
import com.blackcatz.android.hnews.ui.stories.domain.StoryRequest

sealed class StoriesIntent : MviIntent {
    data class RefreshIntent(val story: Story) : StoriesIntent()
    data class LoadStories(val storyRequest: StoryRequest) : StoriesIntent()
}