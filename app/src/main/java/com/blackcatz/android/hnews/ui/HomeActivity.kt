package com.blackcatz.android.hnews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.blackcatz.android.hnews.ui.home.HomeScreen
import com.blackcatz.android.hnews.ui.theme.HnewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HnewsTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}