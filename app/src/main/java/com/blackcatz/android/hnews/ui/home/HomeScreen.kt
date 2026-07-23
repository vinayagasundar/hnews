package com.blackcatz.android.hnews.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.ui.theme.HnewsTheme
import com.blackcatz.android.hnews.ui.theme.spacing
import kotlinx.coroutines.FlowPreview

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
) {
    val lazyStories = viewModel.storiesPager.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()

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
        LazyColumn(
            contentPadding = contentPadding,
            state = lazyListState,
        ) {
            items(
                count = lazyStories.itemCount,
                key = lazyStories.itemKey {
                    it.id.toString()
                },
            ) { index ->
                val story = lazyStories[index] ?: return@items
                StoryListItem(
                    title = story.title,
                    author = story.author,
                    score = story.noOfVotes.toString(),
                    domain = story.domain,
                )
            }

            when (val append = lazyStories.loadState.append) {
                is LoadState.Loading -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium),
                        contentAlignment = Alignment.Center,
                    ) {
                        TextButton(onClick = { lazyStories.retry() }) {
                            Text(text = "Retry (${append.error.localizedMessage})")
                        }
                    }
                }

                is LoadState.NotLoading -> Unit
            }
        }
    }
}

@Composable
private fun StoryListItem(
    title: String,
    author: String,
    score: String,
    domain: String,
) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_up),
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

            Text(
                text = score,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Column(
            modifier = Modifier.weight(3.5f),
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyLarge,
                )

                Text(
                    text = domain,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}


@Preview
@Composable
private fun DemoStoryListItem() {
    HnewsTheme {
        Surface {
            Column {
                StoryListItem(
                    title = "Everything I wish I knew when learning C",
                    author = "John Doe",
                    score = "1",
                    domain = "github.com",
                )
                StoryListItem(
                    title = "Everything I wish I knew when learning C",
                    author = "John Doe",
                    score = "100",
                    domain = "wordpress.com",
                )
            }
        }
    }
}
