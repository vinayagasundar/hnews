package com.blackcatz.android.hnews.data

import com.blackcatz.android.hnews.model.Item

class MockItem {

    companion object {

        val itemOne = Item(
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

        val itemTwo = Item(
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

        val allItems = listOf<Item>(itemOne, itemTwo)
    }
}