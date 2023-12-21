package com.blackcatz.android.hnews.ui.stories.di

import com.blackcatz.android.hnews.mvi.rx.SchedulerProvider
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.stories.StoriesActionProcessorHolder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class StoryViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideTopStoriesActionProcessor(itemRepo: ItemRepo, schedulerProvider: SchedulerProvider) =
        StoriesActionProcessorHolder(itemRepo, schedulerProvider)
}