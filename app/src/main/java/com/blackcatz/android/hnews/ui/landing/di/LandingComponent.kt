package com.blackcatz.android.hnews.ui.landing.di

import androidx.lifecycle.ViewModelProvider
import com.blackcatz.android.hnews.ui.landing.LandingActivity
import com.blackcatz.android.hnews.ui.landing.LandingViewModel
import dagger.Component
import dagger.Module
import dagger.Provides


interface LandingDependencies {
}


@Component(modules = [LandingModule::class], dependencies = [LandingDependencies::class])
interface LandingComponent {

    @Component.Factory
    interface Builder {
        fun create(dependencies: LandingDependencies): LandingComponent
    }

    fun inject(landingActivity: LandingActivity)
}


@Module
internal class LandingModule {
    @Provides
    fun provideViewModelFactory(): ViewModelProvider.Factory = LandingViewModel.Factory()
}