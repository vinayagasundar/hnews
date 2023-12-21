package com.blackcatz.android.hnews.ui.stories

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvi.android.MviAppFragment
import com.blackcatz.android.hnews.ui.stories.domain.StoryRequest
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

@AndroidEntryPoint
class StoriesFragment : MviAppFragment<StoriesIntent, StoriesViewState, StoriesViewModel>(),
    ItemAdapter.Callback {

    private val viewModel by viewModels<StoriesViewModel>()

    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var storiesRecyclerView: RecyclerView

    private val refreshIntentPublisher = BehaviorSubject.create<StoriesIntent.RefreshIntent>()

    private val loadMoreIntentPublisher = PublishSubject.create<StoriesIntent.LoadStories>()

    private val story: Story by lazy {
        val storyType: String? = arguments?.getString(KEY_STORY)
        storyType?.let { Story.fromType(it) } ?: Story.TOP
    }

    private val adapter: ItemAdapter = ItemAdapter(this)

    companion object {
        private const val KEY_STORY = "story"

        fun create(story: Story): StoriesFragment {
            val storyFragment = StoriesFragment()
            storyFragment.arguments = Bundle().apply {
                putString(KEY_STORY, story.toString())
            }
            return storyFragment
        }
    }

    override fun createViewModel(): StoriesViewModel {
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout = view.findViewById(R.id.refresh_layout)
        storiesRecyclerView = view.findViewById(R.id.stories_recycler_view)

        refreshLayout.setOnRefreshListener {
            refreshIntentPublisher.onNext(StoriesIntent.RefreshIntent(story))
        }

        storiesRecyclerView.adapter = adapter
        storiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        storiesRecyclerView.addOnScrollListener(object : LoadMoreScrollListener() {
            override fun loadMore() {
                val page: Int = storiesRecyclerView.tag as? Int ?: 1
                val storyRequest = StoryRequest(page = page, story = story)
                loadMoreIntentPublisher.onNext(StoriesIntent.LoadStories(storyRequest))
            }
        })

        refreshIntentPublisher.onNext(StoriesIntent.RefreshIntent(story))
    }

    override fun injectDependencies() {
    }

    override fun intents(): Observable<StoriesIntent> = Observable.merge(
        refreshIntents(),
        loadMoreIntents()
    )

    override fun render(state: StoriesViewState) {
        if (state.isLoading) {
            refreshLayout.isRefreshing = true
            storiesRecyclerView.visibility = View.GONE
        } else {
            refreshLayout.isRefreshing = false
            storiesRecyclerView.visibility = View.VISIBLE
        }

        storiesRecyclerView.visibility = View.VISIBLE

        if (state.itemList.isNotEmpty()) {
            adapter.submitList(state.itemList)
        }
    }

    override fun onItemSelected(item: Item) {
        val url = item.url
        url?.let { itemUrl ->
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.primaryColor))
            val intent = builder.build()
            intent.launchUrl(requireContext(), Uri.parse(itemUrl))
        }
    }

    private fun refreshIntents() = refreshIntentPublisher

    private fun loadMoreIntents() = loadMoreIntentPublisher.distinctUntilChanged()
}