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

        item_bottom_nav.visibility = View.VISIBLE
    }

    private fun injectDependencies() {
        DaggerLandingComponent.builder()
            .plusDependencies((application as AppComponentProvider).provideAppComponent())
            .build()
            .inject(this)
    }
}