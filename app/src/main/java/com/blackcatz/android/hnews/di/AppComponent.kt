package com.blackcatz.android.hnews.di

import com.blackcatz.android.hnews.network.NetworkModule
import com.blackcatz.android.hnews.ui.landing.di.LandingDependencies
import com.blackcatz.android.hnews.ui.stories.di.StoriesDependenciesComponent
import dagger.Component


@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent : StoriesDependenciesComponent, LandingDependencies {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}