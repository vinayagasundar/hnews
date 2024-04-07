package com.blackcatz.android.hnews.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blackcatz.android.hnews.ui.common.HAppBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    backgroundColor: Color = MaterialTheme.colorScheme.background
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.observeHomeState()
    })

    Scaffold(
        modifier = modifier.background(backgroundColor),
        topBar = { HAppBar() }
    ) {
        StoryList(
            storyList = state.stories,
            contentPadding = it
        )
    }
}


@Composable
private fun StoryList(
    storyList: List<Story>,
    contentPadding: PaddingValues
) {
    LazyColumn(contentPadding = contentPadding) {
        items(storyList) { story ->
            StoryListItem(
                title = story.title,
                noOfVotes = story.noOfVotes.toLong(),
                totalComment = story.totalComment.toLong(),
                url = story.url
            )

            Divider()
        }
    }
}