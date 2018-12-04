package com.blackcatz.android.hnews.ui.stories

import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.adapter.FastListAdapter
import com.blackcatz.android.hnews.adapter.bind
import com.blackcatz.android.hnews.adapter.update
import com.blackcatz.android.hnews.di.AppComponentProvider
import com.blackcatz.android.hnews.model.Item
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvi.android.MviAppFragment
import com.blackcatz.android.hnews.ui.stories.di.DaggerStoriesComponent
import com.blackcatz.android.hnews.ui.stories.domain.StoryRequest
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_stories.*
import timber.log.Timber
import javax.inject.Inject

class StoriesFragment : MviAppFragment<StoriesIntent, StoriesViewState, StoriesViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val refreshIntentPublisher = PublishSubject.create<StoriesIntent.RefreshIntent>()

    private val loadMoreIntentPublisher = PublishSubject.create<StoriesIntent.LoadMoreIntent>()

    private val story: Story by lazy {
        val storyType: String? = arguments?.getString(KEY_STORY)
        storyType?.let { Story.fromType(it) } ?: Story.TOP
    }

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refresh_layout.setOnRefreshListener {
            refreshIntentPublisher.onNext(StoriesIntent.RefreshIntent(true, story))
        }

        stories_recycler_view.bind(emptyList<Item>()).map(R.layout.item_stories, { true }, { item ->
            bindViewHolder(this, item)
        })

        stories_recycler_view.addOnScrollListener(object : LoadMoreScrollListener() {
            override fun loadMore() {
                val page: Int = stories_recycler_view.tag as? Int ?: 1
                val storyRequest = StoryRequest(page = page, story = story)
                loadMoreIntentPublisher
                    .onNext(StoriesIntent.LoadMoreIntent(storyRequest))
            }
        })
    }

    override fun injectDependencies() {
        DaggerStoriesComponent.builder()
            .plusDependencies((context?.applicationContext as AppComponentProvider).provideAppComponent())
            .build()
            .inject(this)
    }

    override fun intents(): Observable<StoriesIntent> = Observable.merge(
        initialIntents(),
        refreshIntents(),
        loadMoreIntents()
    )

    override fun render(state: StoriesViewState) {
        Timber.i("Here $state")
        if (state.isLoading) {
            refresh_layout.isRefreshing = true
            stories_recycler_view.visibility = View.GONE
        } else {
            refresh_layout.isRefreshing = false
            stories_recycler_view.visibility = View.VISIBLE
        }

        stories_recycler_view.visibility = View.VISIBLE

        if (state.itemList.isNotEmpty()) {
            (stories_recycler_view.adapter as? FastListAdapter<Item>)?.update(state.itemList) { o, n ->
                o.id == n.id
            }
            stories_recycler_view.tag = state.nextPage
            refresh_layout.isRefreshing = false
            Timber.i("Good ${stories_recycler_view.adapter?.itemCount ?: -1}")
//            stories_recycler_view.scrollToPosition(0)
        }
    }

    private fun bindViewHolder(view: View, item: Item) {
        val title = view.findViewById<TextView>(R.id.title)
        val author = view.findViewById<TextView>(R.id.author)
        val commentCount = view.findViewById<TextView>(R.id.comments)
        val createdOn = view.findViewById<TextView>(R.id.created_on)

        title?.text = item.title
        author?.text = "by ${item.by}"


        item.kids?.let {
            commentCount?.text = "${it.size} comments"
        }

        view.setOnClickListener {
            val url = item.url
            url?.let { itemUrl ->
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ContextCompat.getColor(view.context, R.color.primaryColor))
                val intent = builder.build()
                intent.launchUrl(this.context, Uri.parse(itemUrl))
            }
        }

        createdOn?.text =
                item.time?.let {
                    DateUtils.getRelativeTimeSpanString(
                        this@StoriesFragment.context,
                        it * 1000,
                        true
                    )
                }
    }

    private fun initialIntents(): Observable<StoriesIntent.InitialIntent> =
        Observable.just(StoriesIntent.InitialIntent(story))

    private fun refreshIntents(): Observable<StoriesIntent.RefreshIntent> = refreshIntentPublisher

    private fun loadMoreIntents(): Observable<StoriesIntent.LoadMoreIntent> = loadMoreIntentPublisher
        .distinctUntilChanged()
}