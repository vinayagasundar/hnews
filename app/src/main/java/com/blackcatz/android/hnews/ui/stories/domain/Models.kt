package com.blackcatz.android.hnews.ui.stories.domain

import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.model.Story

const val DEFAULT_ITEM_SIZE = 20

data class StoryRequest(
    val page: Int = 0,
    val story: Story,
    val size: Int = DEFAULT_ITEM_SIZE,
    val forceUpdate: Boolean = false
)

data class StoryResponse(
    val page: Int = 0,
    val stories: List<Item>
)