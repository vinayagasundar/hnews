package com.blackcatz.android.hnews.ui.stories

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class LoadMoreScrollListener : RecyclerView.OnScrollListener() {

    abstract fun loadMore()

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        (recyclerView.layoutManager as? LinearLayoutManager)?.let {
            val total = recyclerView.adapter?.itemCount ?: -1
            val last = it.findLastVisibleItemPosition()

            if (last > (total - 5) && (total > -1)) {
                loadMore()
            }
        }
    }
}