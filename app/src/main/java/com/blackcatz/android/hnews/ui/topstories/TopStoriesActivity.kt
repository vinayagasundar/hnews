package com.blackcatz.android.hnews.ui.topstories

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.adapter.bind
import com.blackcatz.android.hnews.di.AppComponentProvider
import com.blackcatz.android.hnews.mvi.android.MviAppActivity
import com.blackcatz.android.hnews.ui.topstories.di.DaggerTopStoriesComponent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_topstories.*
import timber.log.Timber
import javax.inject.Inject

class TopStoriesActivity : MviAppActivity<TopStoriesIntent, TopStoriesViewState, TopStoriesViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val refreshIntentPublisher = PublishSubject.create<TopStoriesIntent.RefreshIntent>()

    override fun createViewModel(): TopStoriesViewModel {
        return ViewModelProviders.of(this, viewModelFactory)[TopStoriesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topstories)
//        refreshIntentPublisher.onNext(TopStoriesIntent.RefreshIntent(true))
    }

    override fun injectDependencies() {
        DaggerTopStoriesComponent.builder()
            .plusDependencies((application as AppComponentProvider).provideAppComponent())
            .build()
            .inject(this)
    }

    override fun intents(): Observable<TopStoriesIntent> = Observable.merge(
        initialIntents(),
        refreshIntents()
    )

    override fun render(state: TopStoriesViewState) {
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
                                this@TopStoriesActivity,
                                it * 1000,
                                true
                            )
                        }
            })
        }
    }

    private fun initialIntents(): Observable<TopStoriesIntent.InitialIntent> =
        Observable.just(TopStoriesIntent.InitialIntent)

    private fun refreshIntents(): Observable<TopStoriesIntent.RefreshIntent> =
        Observable.just(TopStoriesIntent.RefreshIntent(false))
            .map { it }
            .mergeWith(refreshIntentPublisher)
}