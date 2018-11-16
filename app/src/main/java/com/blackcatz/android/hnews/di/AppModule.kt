package com.blackcatz.android.hnews.di

import com.blackcatz.android.hnews.network.HackerAPI
import com.blackcatz.android.hnews.repo.StoriesRepo
import com.blackcatz.android.hnews.repo.StoriesRepoImpl
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideStoriesRepo(hackerAPI: HackerAPI): StoriesRepo = StoriesRepoImpl(hackerAPI)
}