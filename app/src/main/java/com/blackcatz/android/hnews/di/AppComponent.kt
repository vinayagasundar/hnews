package com.blackcatz.android.hnews.di

import com.blackcatz.android.hnews.network.NetworkModule
import com.blackcatz.android.hnews.ui.topstories.di.TopStoriesDependencies
import dagger.Component


@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent : TopStoriesDependencies {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}