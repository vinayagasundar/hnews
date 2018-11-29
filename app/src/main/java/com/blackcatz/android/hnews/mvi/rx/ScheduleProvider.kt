package com.blackcatz.android.hnews.mvi.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulerProvider {
    fun io(): Scheduler

    fun computation(): Scheduler

    fun trampoline(): Scheduler

    fun main(): Scheduler
}


class SchedulerProviderImpl : SchedulerProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun trampoline(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun main(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}