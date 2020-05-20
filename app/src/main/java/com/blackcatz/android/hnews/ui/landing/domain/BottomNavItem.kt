package com.blackcatz.android.hnews.ui.landing.domain

import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.model.Story


val TOP_STORIES = BottomNavItem(0,"Top", R.drawable.ic_top, Story.TOP)
val NEW_STORIES = BottomNavItem(1, "New", R.drawable.ic_top, Story.NEW)
val BEST_STORIES = BottomNavItem(2, "Best", R.drawable.ic_top, Story.BEST)
val ASK_STORIES = BottomNavItem(3, "Ask", R.drawable.ic_ask, Story.ASK)
val SHOW_STORIES = BottomNavItem(4, "Show", R.drawable.ic_show, Story.SHOW)
val JOB_STORIES = BottomNavItem(5, "Job", R.drawable.ic_job, Story.JOB)

val ALL_STORIES = arrayOf(TOP_STORIES, ASK_STORIES, SHOW_STORIES)

data class BottomNavItem(
    val id: Int,
    val title: String,
    val icon: Int,
    val type: Story
)


