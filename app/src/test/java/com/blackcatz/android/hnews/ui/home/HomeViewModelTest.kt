package com.blackcatz.android.hnews.ui.home

import androidx.paging.PagingData
import com.blackcatz.android.hnews.data.repository.StoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `storiesPager delegates to repository stream`() = runTest {
        val repository = mockk<StoryRepository>()
        every { repository.getStoriesStream() } returns flowOf(PagingData.empty())

        val viewModel = HomeViewModel(repository)
        viewModel.storiesPager.first()

        verify { repository.getStoriesStream() }
    }
}
