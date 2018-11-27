package com.blackcatz.android.hnews.model

enum class Story(private val type: String) {
    TOP("topstories"),
    NEW("newstories"),
    BEST("beststories"),
    ASK("askstories"),
    SHOW("showstories"),
    JOB("jobstories");


    override fun toString(): String {
        return type
    }

    companion object {
        fun fromType(type: String): Story {
            return Story.values().first {
                it.type == type
            }
        }
    }
}