package com.blackcatz.android.hnews.ui.stories.di

import androidx.lifecycle.ViewModelProvider
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.stories.StoriesActionProcessorHolder
import com.blackcatz.android.hnews.ui.stories.StoriesFragment
import com.blackcatz.android.hnews.ui.stories.StoriesViewModel
import dagger.Component
import dagger.Module
import dagger.Provides


interface StoriesDependenciesComponent {
    fun provideStoriesRepo(): ItemRepo
}


@Component(dependencies = [StoriesDependenciesComponent::class], modules = [StoriesModule::class])
interface StoriesComponent {

    @Component.Builder
    interface Builder {
        fun plusDependencies(storiesComponent: StoriesDependenciesComponent): Builder

        fun build(): StoriesComponent
    }

    fun inject(storiesFragment: StoriesFragment)
}

@Module
class StoriesModule {

    @Provides
    fun provideTopStoriesActionProcessor(itemRepo: ItemRepo) = StoriesActionProcessorHolder(itemRepo)

    @Provides
    fun provideTopStoriesViewModelFactory(storiesActionProcessorHolder: StoriesActionProcessorHolder): ViewModelProvider.Factory =
        StoriesViewModel.Factory(storiesActionProcessorHolder)
}