package com.blackcatz.android.hnews.di

import com.blackcatz.android.hnews.mvi.rx.SchedulerProvider
import com.blackcatz.android.hnews.mvi.rx.SchedulerProviderImpl
import com.blackcatz.android.hnews.network.HackerAPI
import com.blackcatz.android.hnews.repo.ItemRepo
import com.blackcatz.android.hnews.repo.ItemRepoImpl
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideStoriesRepo(hackerAPI: HackerAPI): ItemRepo = ItemRepoImpl(hackerAPI)

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = SchedulerProviderImpl()
}