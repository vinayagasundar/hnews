package com.blackcatz.android.hnews.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcatz.android.hnews.network.HackerAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
            val topStories = topStoryIds.map {
                val item = hackerAPI.getItem(it.toString())
                Story(
                    item.id.toLong(),
                    item.title.orEmpty(),
                    item.score?.toInt() ?: 0,
                    item.kids.orEmpty().size,
                    item.url.orEmpty()
                )
            }
            _state.update { it.copy(stories = topStories) }
        }
    }
}