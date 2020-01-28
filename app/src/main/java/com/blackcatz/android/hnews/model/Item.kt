package com.blackcatz.android.hnews.model

import com.google.gson.annotations.SerializedName

/**
 * Simple data item for API response
 * @author vinayagasundar
 */
data class Item(
    @SerializedName("id") val id: Int,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("type") val type: String,
    @SerializedName("by") val by: String?,
    @SerializedName("time") val time: Long?,
    @SerializedName("text") val text: String?,
    @SerializedName("dead") val dead: Boolean,
    @SerializedName("parent") val parent: Long,
    @SerializedName("poll") val poll: Long,
    @SerializedName("kids") val kids: List<Long>? = emptyList(),
    @SerializedName("url") val url: String?,
    @SerializedName("score") val score: Long,
    @SerializedName("title") val title: String?,
    @SerializedName("parts") val parts: List<Long> = emptyList(),
    @SerializedName("descendants") val descendants: Long
)