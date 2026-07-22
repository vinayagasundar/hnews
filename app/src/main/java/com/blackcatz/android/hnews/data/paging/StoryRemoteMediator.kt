package com.blackcatz.android.hnews.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.blackcatz.android.hnews.data.local.HNewsDatabase
import com.blackcatz.android.hnews.data.local.RemoteKeysEntity
import com.blackcatz.android.hnews.data.local.StoryEntity
import com.blackcatz.android.hnews.data.mapper.toStoryEntity
import com.blackcatz.android.hnews.data.network.HackerAPI
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val hackerAPI: HackerAPI,
    private val database: HNewsDatabase,
) : RemoteMediator<Int, StoryEntity>() {

    // Cached for the lifetime of this Mediator instance; refetched on every REFRESH so
    // pull-to-refresh/relaunch picks up ranking changes, reused across APPEND calls within
    // the same session so page-offset math stays consistent.
    private var topStoryIds: List<Long>? = null

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>,
    ): MediatorResult {
        return try {
            val ids = when (loadType) {
                LoadType.REFRESH -> hackerAPI.getTopStories().also { topStoryIds = it }
                else -> topStoryIds ?: hackerAPI.getTopStories().also { topStoryIds = it }
            }

            val pageSize = state.config.pageSize

            val pageStart: Int = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKeys.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            if (pageStart >= ids.size) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            val pageEnd = (pageStart + pageSize).coerceAtMost(ids.size)
            val pageIds = ids.subList(pageStart, pageEnd)

            val fetched = coroutineScope {
                pageIds.map { id ->
                    async { runCatching { hackerAPI.getItem(id) }.getOrNull() }
                }.awaitAll()
            }.filterNotNull()

            if (fetched.isEmpty() && pageIds.isNotEmpty()) {
                return MediatorResult.Error(IOException("Failed to load story details"))
            }

            val endOfPaginationReached = pageEnd >= ids.size
            val nextKey = if (endOfPaginationReached) null else pageEnd

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.storyDao().clearAll()
                }
                val entities = fetched.mapIndexed { index, item ->
                    item.toStoryEntity(position = pageStart + index)
                }
                val keys = entities.map { RemoteKeysEntity(it.id, prevKey = null, nextKey = nextKey) }
                database.remoteKeysDao().insertAll(keys)
                database.storyDao().insertAll(entities)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, StoryEntity>,
    ): RemoteKeysEntity? = state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
        ?.let { database.remoteKeysDao().remoteKeysStoryId(it.id) }
}
