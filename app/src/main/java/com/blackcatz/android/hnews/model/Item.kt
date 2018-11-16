package com.blackcatz.android.hnews.model

/**
 * Simple data item for API response
 * @author vinayagasundar
 */
data class Item(
    val id: Int,
    val deleted: Boolean,
    val type: String,
    val by: String?,
    val time: Long?,
    val text: String?,
    val dead: Boolean,
    val parent: Long,
    val poll: Long,
    val kids: List<Long>? = emptyList(),
    val url: String?,
    val score: Long,
    val title: String?,
    val parts: List<Long> = emptyList(),
    val descendants: Long
)