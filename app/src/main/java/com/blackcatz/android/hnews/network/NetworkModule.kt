package com.blackcatz.android.hnews.network

import com.blackcatz.android.hnews.BuildConfig
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideGson() = Gson()

    @IntoSet
    @Provides
    @Singleton
    fun provideHttpLoggerInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp(interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient {
        val builder = OkHttpClient.Builder()
        interceptors.forEach {
            builder.addInterceptor(it)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://hacker-news.firebaseio.com/v0/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideHackerAPI(retrofit: Retrofit): HackerAPI {
        return retrofit.create(HackerAPI::class.java)
    }
}