package com.blackcatz.android.hnews.ui.stories

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.model.Item

private val DIFF_ITEM_CALLBACK_FOR_ITEM = object : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Item, newItem: Item): Any? {
        return newItem
    }
}

private val CALLBACK_STUB_IMPL = object : ItemAdapter.Callback {
    override fun onItemSelected(item: Item) {

    }
}

internal class ItemAdapter(private val callback: Callback = CALLBACK_STUB_IMPL) :
    ListAdapter<Item, ItemAdapter.ViewHolder>(DIFF_ITEM_CALLBACK_FOR_ITEM) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stories, parent, false)
        return ViewHolder(view, callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val view: View,
        private val callback: Callback
    ) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.title)
        private val author = view.findViewById<TextView>(R.id.author)
        private val commentCount = view.findViewById<TextView>(R.id.comments)
        private val createdOn = view.findViewById<TextView>(R.id.created_on)

        private val context
            get() = view.context

        fun bind(item: Item) {
            title.text = item.title
            author.text = context.getString(R.string.story_label_by, item.by)
            item.kids?.let {
                commentCount.text =
                    context.getString(R.string.story_label_comments, it.size)
            }
            item.time?.let {
                createdOn.text = DateUtils.getRelativeTimeSpanString(
                    context,
                    it * 1000,
                    true
                )
            }

            view.setOnClickListener {
                callback.onItemSelected(item)
            }
        }
    }

    interface Callback {
        fun onItemSelected(item: Item)
    }
}