package com.blackcatz.android.hnews.di

import com.blackcatz.android.hnews.network.NetworkModule
import com.blackcatz.android.hnews.ui.landing.di.LandingDependencies
import com.blackcatz.android.hnews.ui.topstories.di.TopStoriesDependencies
import dagger.Component


@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent : TopStoriesDependencies, LandingDependencies {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}