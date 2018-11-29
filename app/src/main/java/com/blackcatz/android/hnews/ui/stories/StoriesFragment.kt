package com.blackcatz.android.hnews.ui.stories

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.adapter.bind
import com.blackcatz.android.hnews.di.AppComponentProvider
import com.blackcatz.android.hnews.model.Story
import com.blackcatz.android.hnews.mvi.android.MviAppFragment
import com.blackcatz.android.hnews.ui.stories.di.DaggerStoriesComponent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_stories.*
import timber.log.Timber
import javax.inject.Inject

class StoriesFragment : MviAppFragment<StoriesIntent, StoriesViewState, StoriesViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val refreshIntentPublisher = PublishSubject.create<StoriesIntent.RefreshIntent>()

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
    }

    override fun injectDependencies() {
        DaggerStoriesComponent.builder()
            .plusDependencies((context?.applicationContext as AppComponentProvider).provideAppComponent())
            .build()
            .inject(this)
    }

    override fun intents(): Observable<StoriesIntent> = Observable.merge(
        initialIntents(),
        refreshIntents()
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
            stories_recycler_view.bind(state.itemList).map(R.layout.item_stories, { true }, { item ->
                val title = findViewById<TextView>(R.id.title)
                val author = findViewById<TextView>(R.id.author)
                val commentCount = findViewById<TextView>(R.id.comments)
                val createdOn = findViewById<TextView>(R.id.created_on)

                title?.text = item.title
                author?.text = "by ${item.by}"


                item.kids?.let {
                    commentCount?.text = "${it.size} comments"
                }

                createdOn?.text =
                        item.time?.let {
                            DateUtils.getRelativeTimeSpanString(
                                this@StoriesFragment.context,
                                it * 1000,
                                true
                            )
                        }
            })
        }
    }

    private fun initialIntents(): Observable<StoriesIntent.InitialIntent> =
        Observable.just(StoriesIntent.InitialIntent(story))

    private fun refreshIntents(): Observable<StoriesIntent.RefreshIntent> = refreshIntentPublisher
}