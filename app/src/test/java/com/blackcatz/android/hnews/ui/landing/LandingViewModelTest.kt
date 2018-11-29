package com.blackcatz.android.hnews.ui.landing

import com.blackcatz.android.hnews.ui.landing.domain.ALL_STORIES
import org.junit.Assert.assertEquals
import org.junit.Test

class LandingViewModelTest {

    private val landingViewModel = LandingViewModel()

    @Test
    fun getNavViews() {
        assertEquals(ALL_STORIES.toList(), landingViewModel.getNavViews())
    }
}