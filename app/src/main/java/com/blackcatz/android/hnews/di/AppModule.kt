package com.blackcatz.android.hnews.di

import com.blackcatz.android.hnews.mvi.rx.SchedulerProvider
import com.blackcatz.android.hnews.mvi.rx.SchedulerProviderImpl
import com.blackcatz.android.hnews.network.HackerAPI
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.repo.ItemRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideStoriesRepo(hackerAPI: HackerAPI): ItemRepo = ItemRepoImpl(hackerAPI)

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = SchedulerProviderImpl()
}