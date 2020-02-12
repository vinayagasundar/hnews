package com.blackcatz.android.hnews.ui.stories.di

import androidx.lifecycle.ViewModelProvider
import com.blackcatz.android.hnews.mvi.rx.RxLifeCycle
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.ui.stories.StoriesActionProcessorHolder
import com.blackcatz.android.hnews.ui.stories.StoriesFragment
import com.blackcatz.android.hnews.ui.stories.StoriesViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides


interface StoriesDependenciesComponent {
    fun provideStoriesRepo(): ItemRepo
}


@Component(dependencies = [StoriesDependenciesComponent::class], modules = [StoriesModule::class])
interface StoriesComponent {

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: StoriesDependenciesComponent,
            @BindsInstance rxBind: RxLifeCycle
        ): StoriesComponent
    }

    fun inject(storiesFragment: StoriesFragment)
}

@Module
class StoriesModule {

    @Provides
    fun provideTopStoriesActionProcessor(itemRepo: ItemRepo, rxLifeCycle: RxLifeCycle) =
        StoriesActionProcessorHolder(itemRepo, rxLifeCycle)

    @Provides
    fun provideTopStoriesViewModelFactory(
        storiesActionProcessorHolder: StoriesActionProcessorHolder,
        rxLifeCycle: RxLifeCycle
    ): ViewModelProvider.Factory =
        StoriesViewModel.Factory(storiesActionProcessorHolder, rxLifeCycle)
}