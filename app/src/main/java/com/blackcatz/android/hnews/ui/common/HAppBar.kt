package com.blackcatz.android.hnews.ui.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.ui.theme.HnewsTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HAppBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.app_name),
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Text(text = title)
        },
    )
}

@Preview
@Composable
private fun DemoHAppBar() {
    HnewsTheme(false, false) {
        HAppBar()
    }
}