package com.blackcatz.android.hnews.ui.home

data class HomeState(
    val stories: List<Story>
)

data class Story(
    val id: Long,
    val title: String,
    val noOfVotes: Int,
    val totalComment: Int,
    val url: String
)
