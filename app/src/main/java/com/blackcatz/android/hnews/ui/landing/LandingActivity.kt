package com.blackcatz.android.hnews.ui.landing

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.di.AppComponentProvider
import com.blackcatz.android.hnews.ui.landing.di.DaggerLandingComponent
import com.blackcatz.android.hnews.ui.landing.domain.ASK_STORIES
import com.blackcatz.android.hnews.ui.landing.domain.JOB_STORIES
import com.blackcatz.android.hnews.ui.landing.domain.SHOW_STORIES
import com.blackcatz.android.hnews.ui.landing.domain.TOP_STORIES
import kotlinx.android.synthetic.main.activity_landing.*
import javax.inject.Inject

class LandingActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: LandingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        viewModel = ViewModelProviders.of(this, factory)[LandingViewModel::class.java]

        getAllTabs()
    }

    private fun getAllTabs() {
        loading_bar.visibility = View.VISIBLE
        loading_bar.visibility = View.GONE

        val list = viewModel.getNavViews()

        list.forEach {
            item_bottom_nav.menu.add(Menu.NONE, 1, Menu.NONE, it.title)
                .setIcon(it.icon)
        }

        val navAdapter = NavAdapter(supportFragmentManager)
        stories_holder.adapter = navAdapter
        stories_holder.visibility = View.VISIBLE

        item_bottom_nav.setOnNavigationItemSelectedListener {
            stories_holder.currentItem = when (it.title) {
                TOP_STORIES.title -> 0
                ASK_STORIES.title -> 1
                SHOW_STORIES.title -> 2
                JOB_STORIES.title -> 3
                else -> 0
            }
            true
        }

        item_bottom_nav.visibility = View.VISIBLE
    }

    private fun injectDependencies() {
        val dependencies = (application as AppComponentProvider).provideAppComponent()
        DaggerLandingComponent.factory()
            .create(dependencies)
            .inject(this)
    }
}