package com.blackcatz.android.hnews.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blackcatz.android.hnews.ui.landing.domain.ALL_STORIES
import com.blackcatz.android.hnews.ui.landing.domain.BottomNavItem


class LandingViewModel : ViewModel() {

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LandingViewModel() as T
        }
    }


    fun getNavViews(): List<BottomNavItem> {
        return ALL_STORIES.asList()
    }
}