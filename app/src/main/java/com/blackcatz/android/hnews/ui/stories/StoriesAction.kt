package com.blackcatz.android.hnews.ui.stories

import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvi.MviAction
import com.blackcatz.android.hnews.ui.stories.domain.StoryRequest

sealed class StoriesAction: MviAction {
    data class LoadStoriesAction(val story: Story, val forceUpdate: Boolean): StoriesAction()
    data class LoadMoreStoriesAction(val storyRequest: StoryRequest): StoriesAction()
}