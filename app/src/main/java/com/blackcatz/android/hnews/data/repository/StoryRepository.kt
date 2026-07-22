package com.blackcatz.android.hnews.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.blackcatz.android.hnews.data.local.HNewsDatabase
import com.blackcatz.android.hnews.data.local.StoryDao
import com.blackcatz.android.hnews.data.mapper.toStory
import com.blackcatz.android.hnews.data.model.Story
import com.blackcatz.android.hnews.data.network.HackerAPI
import com.blackcatz.android.hnews.data.paging.StoryRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val hackerAPI: HackerAPI,
    private val database: HNewsDatabase,
    private val storyDao: StoryDao,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getStoriesStream(): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            remoteMediator = StoryRemoteMediator(hackerAPI, database),
            pagingSourceFactory = { storyDao.pagingSource() },
        ).flow.map { pagingData -> pagingData.map { it.toStory() } }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
