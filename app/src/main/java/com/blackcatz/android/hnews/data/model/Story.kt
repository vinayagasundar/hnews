package com.blackcatz.android.hnews.data.model

data class Story(
    val id: Long,
    val title: String,
    val author: String,
    val noOfVotes: Int,
    val totalComment: Int,
    val domain: String,
)
