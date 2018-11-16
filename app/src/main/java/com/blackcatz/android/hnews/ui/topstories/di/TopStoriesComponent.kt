package com.blackcatz.android.hnews.ui.topstories.di

import androidx.lifecycle.ViewModelProvider
import com.blackcatz.android.hnews.repo.StoriesRepo
import com.blackcatz.android.hnews.ui.topstories.TopStoriesActionProcessorHolder
import com.blackcatz.android.hnews.ui.topstories.TopStoriesActivity
import com.blackcatz.android.hnews.ui.topstories.TopStoriesViewModel
import dagger.Component
import dagger.Module
import dagger.Provides


interface TopStoriesDependencies {
    fun provideStoriesRepo(): StoriesRepo
}


@Component(dependencies = [TopStoriesDependencies::class], modules = [TopStoriesModule::class])
interface TopStoriesComponent {

    @Component.Builder
    interface Builder {
        fun plusDependencies(topStoriesDependencies: TopStoriesDependencies): Builder

        fun build(): TopStoriesComponent
    }

    fun inject(topStoriesActivity: TopStoriesActivity)
}

@Module
class TopStoriesModule {

    @Provides
    fun provideTopStoriesActionProcessor(storiesRepo: StoriesRepo) = TopStoriesActionProcessorHolder(storiesRepo)

    @Provides
    fun provideTopStoriesViewModelFactory(topStoriesActionProcessorHolder: TopStoriesActionProcessorHolder): ViewModelProvider.Factory =
        TopStoriesViewModel.Factory(topStoriesActionProcessorHolder)
}