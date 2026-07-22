package com.blackcatz.android.hnews.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.blackcatz.android.hnews.data.model.Story
import com.blackcatz.android.hnews.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    storyRepository: StoryRepository,
) : ViewModel() {

    val storiesPager: Flow<PagingData<Story>> =
        storyRepository.getStoriesStream().cachedIn(viewModelScope)
}
