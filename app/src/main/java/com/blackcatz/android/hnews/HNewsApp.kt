package com.blackcatz.android.hnews

import android.app.Application
import com.blackcatz.android.hnews.di.AppComponent
import com.blackcatz.android.hnews.di.AppComponentProvider
import com.blackcatz.android.hnews.di.DaggerAppComponent
import timber.log.Timber

class HNewsApp : Application(), AppComponentProvider {
    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().build()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun provideAppComponent(): AppComponent {
        return appComponent
    }
}