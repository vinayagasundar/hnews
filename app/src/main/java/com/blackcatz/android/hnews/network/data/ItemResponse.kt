package com.blackcatz.android.hnews.network.data

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
    @SerializedName("parent") val parent: Long? = null,
    @SerializedName("poll") val poll: Long? = null,
    @SerializedName("kids") val kids: List<Long>? = emptyList(),
    @SerializedName("url") val url: String? = null,
    @SerializedName("score") val score: Long? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("parts") val parts: List<Long>? = emptyList(),
    @SerializedName("descendants") val descendants: Long? = null,
    @SerializedName("deleted") val deleted: Boolean? = null,
)