package com.blackcatz.android.hnews.ui.landing.domain

import com.blackcatz.android.hnews.R


enum class Story(private val type: String) {
    TOP("topstories"),
    NEW("newstories"),
    BEST("beststories"),
    ASK("askstories"),
    SHOW("showstories"),
    JOB("jobstories")
}


val TOP_STORIES = BottomNavItem("Top", R.drawable.ic_top, Story.TOP)
val NEW_STORIES = BottomNavItem("New", R.drawable.ic_top, Story.NEW)
val BEST_STORIES = BottomNavItem("Best", R.drawable.ic_top, Story.BEST)
val ASK_STORIES = BottomNavItem("Ask", R.drawable.ic_ask, Story.ASK)
val SHOW_STORIES = BottomNavItem("Show", R.drawable.ic_show, Story.SHOW)
val JOB_STORIES = BottomNavItem("Job", R.drawable.ic_job, Story.JOB)

val ALL_STORIES = arrayOf(TOP_STORIES, ASK_STORIES, SHOW_STORIES, JOB_STORIES)

data class BottomNavItem(
    val title: String,
    val icon: Int,
    val type: Story
)


