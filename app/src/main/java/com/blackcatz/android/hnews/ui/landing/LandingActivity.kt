package com.blackcatz.android.hnews.ui.landing

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.blackcatz.android.hnews.R
import com.blackcatz.android.hnews.ui.landing.domain.ALL_STORIES
import com.blackcatz.android.hnews.ui.landing.domain.ASK_STORIES
import com.blackcatz.android.hnews.ui.landing.domain.JOB_STORIES
import com.blackcatz.android.hnews.ui.landing.domain.SHOW_STORIES
import com.blackcatz.android.hnews.ui.landing.domain.TOP_STORIES
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    private lateinit var pbLoading: ProgressBar
    private lateinit var vpStoryHolder: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        pbLoading = findViewById(R.id.loading_bar)
        vpStoryHolder = findViewById(R.id.stories_holder)
        bottomNavigationView = findViewById(R.id.item_bottom_nav)

        getAllTabs()
    }

    private fun getAllTabs() {
        pbLoading.visibility = View.VISIBLE

        val list = ALL_STORIES.asList()

        list.forEach {
            bottomNavigationView.menu.add(Menu.NONE, it.id, Menu.NONE, it.title)
                .setIcon(it.icon)
        }

        val navAdapter = NavAdapter(supportFragmentManager)
        vpStoryHolder.adapter = navAdapter
        vpStoryHolder.visibility = View.VISIBLE
        pbLoading.visibility = View.GONE


        bottomNavigationView.setOnNavigationItemSelectedListener {
            vpStoryHolder.currentItem = when (it.title) {
                TOP_STORIES.title -> 0
                ASK_STORIES.title -> 1
                SHOW_STORIES.title -> 2
                JOB_STORIES.title -> 3
                else -> 0
            }
            true
        }

        bottomNavigationView.visibility = View.VISIBLE
    }
}