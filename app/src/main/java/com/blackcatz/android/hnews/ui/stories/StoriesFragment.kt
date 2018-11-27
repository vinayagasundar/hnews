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

    override fun createViewModel(): StoriesViewModel {
        return ViewModelProviders.of(this, viewModelFactory)[StoriesViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stories, container, false)
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
            loading_bar.visibility = View.VISIBLE
            stories_recycler_view.visibility = View.GONE
        } else {
            loading_bar.visibility = View.GONE
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
        Observable.just(StoriesIntent.InitialIntent)

    private fun refreshIntents(): Observable<StoriesIntent.RefreshIntent> =
        Observable.just(StoriesIntent.RefreshIntent(false))
            .map { it }
            .mergeWith(refreshIntentPublisher)
}