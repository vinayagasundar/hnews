package com.blackcatz.android.hnews.data

import com.blackcatz.android.hnews.network.data.ItemResponse

class MockItem {

    companion object {

        val itemResponseOne = ItemResponse(
            id = 100,
            deleted = false,
            type = "type",
            by = "author",
            time = 0,
            text = "Hello World",
            dead = false,
            parent = 1,
            poll = 100,
            kids = null,
            url = null,
            score = 100,
            title = "Better World",
            parts = emptyList(),
            descendants = 1
        )

        val itemResponseTwo = ItemResponse(
            id = 100,
            deleted = false,
            type = "type",
            by = "author",
            time = 0,
            text = "Hello World",
            dead = false,
            parent = 1,
            poll = 100,
            kids = null,
            url = null,
            score = 100,
            title = "Better World",
            parts = emptyList(),
            descendants = 1
        )

        val allItemResponses = listOf<ItemResponse>(itemResponseOne, itemResponseTwo)
    }
}