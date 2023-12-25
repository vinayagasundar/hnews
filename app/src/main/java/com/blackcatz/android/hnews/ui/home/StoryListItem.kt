package com.blackcatz.android.hnews.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.ui.theme.HnewsTheme
import java.net.URL
import java.util.Locale

@Composable
fun StoryListItem(
    title: String,
    noOfVotes: Long = 0,
    totalComment: Long = 0,
    url: String = ""
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            UpVote(noOfVotes)

            Spacer(modifier = Modifier.width(16.dp))

            Comment(totalComment)

            Spacer(modifier = Modifier.width(16.dp))

            Link(url)
        }
    }
}

@Composable
private fun Link(url: String) {
    Row(
        modifier = Modifier
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val domain = remember(key1 = url) {
            try {
                URL(url).host.uppercase(Locale.getDefault())
            } catch (ex: Exception) {
                ""
            }
        }
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_link),
            contentDescription = "",
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = domain,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun UpVote(count: Long = 0) {
    val content = if (count == 0L) {
        "Vote"
    } else {
        count.toString()
    }
    ListItemActionButton(
        content = content,
        icon = ImageVector.vectorResource(id = R.drawable.ic_arrow_up)
    )
}

@Composable
private fun Comment(count: Long = 0) {
    val content = if (count == 0L) {
        ""
    } else {
        count.toString()
    }
    ListItemActionButton(
        content = content,
        icon = ImageVector.vectorResource(id = R.drawable.ic_comment)
    )
}

@Composable
private fun ListItemActionButton(
    content: String,
    icon: ImageVector,
    iconDescription: String = "",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .size(width = 56.dp, height = 24.dp)
            .clickable { onClick() }
            .background(Color(0xFFF1F1F1), CircleShape)
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
        )
    }
}


@Preview
@Composable
private fun DemoStoryListItem() {
    HnewsTheme {
        StoryListItem(
            title = "Everything I wish I knew when learning C",
            noOfVotes = 326,
            totalComment = 18,
            url = "https://developer.android.com/jetpack/androidx/releases/compose-material"
        )
    }
}