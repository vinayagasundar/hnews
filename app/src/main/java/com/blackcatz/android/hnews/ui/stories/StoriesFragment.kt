package com.blackcatz.android.hnews.ui.stories

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.di.AppComponentProvider
import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvi.android.MviAppFragment
import com.blackcatz.android.hnews.ui.stories.di.DaggerStoriesComponent
import com.blackcatz.android.hnews.ui.stories.domain.StoryRequest
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_stories.*
import javax.inject.Inject

class StoriesFragment : MviAppFragment<StoriesIntent, StoriesViewState, StoriesViewModel>(),
    ItemAdapter.Callback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
        return ViewModelProviders.of(this, viewModelFactory)[StoriesViewModel::class.java]
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

        refresh_layout.setOnRefreshListener {
            refreshIntentPublisher.onNext(StoriesIntent.RefreshIntent(story))
        }

        stories_recycler_view.adapter = adapter
        stories_recycler_view.layoutManager = LinearLayoutManager(requireContext())
        stories_recycler_view.addOnScrollListener(object : LoadMoreScrollListener() {
            override fun loadMore() {
                val page: Int = stories_recycler_view.tag as? Int ?: 1
                val storyRequest = StoryRequest(page = page, story = story)
                loadMoreIntentPublisher.onNext(StoriesIntent.LoadStories(storyRequest))
            }
        })

        refreshIntentPublisher.onNext(StoriesIntent.RefreshIntent(story))
    }

    override fun injectDependencies() {
        val dependencies = (context?.applicationContext as AppComponentProvider)
            .provideAppComponent()
        DaggerStoriesComponent.factory()
            .create(dependencies, this)
            .inject(this)
    }

    override fun intents(): Observable<StoriesIntent> = Observable.merge(
        refreshIntents(),
        loadMoreIntents()
    )

    override fun render(state: StoriesViewState) {
        if (state.isLoading) {
            refresh_layout.isRefreshing = true
            stories_recycler_view.visibility = View.GONE
        } else {
            refresh_layout.isRefreshing = false
            stories_recycler_view.visibility = View.VISIBLE
        }

        stories_recycler_view.visibility = View.VISIBLE

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