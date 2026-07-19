package com.blackcatz.android.hnews.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.ui.theme.HnewsTheme
import com.blackcatz.android.hnews.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.observeHomeState()
    })

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name)
                    )
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(contentPadding = contentPadding) {
            items(state.stories) { story ->
                StoryListItem(
                    title = story.title,
                    author = story.author,
                )
            }
        }
    }
}

@Composable
private fun StoryListItem(
    title: String,
    author: String,
) {
    Column(
        modifier = Modifier
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.default)
    ) {
        Text(
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "by",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = author,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                ),
            )
        }
    }
}


@Preview
@Composable
private fun DemoStoryListItem() {
    HnewsTheme {
        Surface {
            StoryListItem(
                title = "Everything I wish I knew when learning C",
                author = "John Doe"
            )
        }
    }
}
