package com.blackcatz.android.hnews.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcatz.android.hnews.data.network.HackerAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val hackerAPI: HackerAPI
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState(emptyList()))

    val state: StateFlow<HomeState>
        get() = _state.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            HomeState(emptyList())
        )

    fun observeHomeState() {
        viewModelScope.launch {
            val topStoryIds = hackerAPI.getTopStories().slice(0..10)
            val topStories = topStoryIds.map { storyId ->
                val item = hackerAPI.getItem(storyId.toString())
                val domain = item.url?.let { url -> URL(url).host }.orEmpty()
                Story(
                    id = item.id.toLong(),
                    title = item.title.orEmpty(),
                    author = item.by,
                    noOfVotes = item.score ?: 0,
                    totalComment = item.kids.orEmpty().size,
                    domain = domain
                )
            }
            _state.update { it.copy(stories = topStories) }
        }
    }
}