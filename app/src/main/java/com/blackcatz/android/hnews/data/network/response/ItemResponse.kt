package com.blackcatz.android.hnews.data.network.response

import com.google.gson.annotations.SerializedName

/**
 * Simple data item for API response
 * @author vinayagasundar
 */
data class ItemResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("by") val by: String,
    @SerializedName("time") val time: Long,
    @SerializedName("text") val text: String? = null,
    @SerializedName("dead") val dead: Boolean? = null,
    @SerializedName("parent") val parent: Int? = null,
    @SerializedName("poll") val poll: Int? = null,
    @SerializedName("kids") val kids: List<Int>? = emptyList(),
    @SerializedName("url") val url: String? = null,
    @SerializedName("score") val score: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("parts") val parts: List<Int>? = emptyList(),
    @SerializedName("descendants") val descendants: Int? = null,
    @SerializedName("deleted") val deleted: Boolean? = null,
)