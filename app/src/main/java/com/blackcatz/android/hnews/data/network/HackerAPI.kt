package com.blackcatz.android.hnews.data.network

import com.blackcatz.android.hnews.data.network.response.ItemResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * API for network request/response
 * @author vinayagasundar
 */
interface HackerAPI {

    @GET("topstories.json")
    suspend fun getTopStories(): List<Long>

    @GET("item/{itemId}.json")
    suspend fun getItem(@Path("itemId") itemId: String): ItemResponse
}