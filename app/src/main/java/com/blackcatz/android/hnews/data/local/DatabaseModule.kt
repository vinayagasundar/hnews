package com.blackcatz.android.hnews.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HNewsDatabase =
        Room.databaseBuilder(context, HNewsDatabase::class.java, "hnews.db").build()

    @Provides
    @Singleton
    fun provideStoryDao(database: HNewsDatabase): StoryDao = database.storyDao()

    @Provides
    @Singleton
    fun provideRemoteKeysDao(database: HNewsDatabase): RemoteKeysDao = database.remoteKeysDao()
}
