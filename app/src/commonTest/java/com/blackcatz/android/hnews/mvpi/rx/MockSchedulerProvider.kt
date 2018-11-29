package com.blackcatz.android.hnews.mvpi.rx

import com.blackcatz.android.hnews.mvi.rx.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class MockSchedulerProvider : SchedulerProvider {
    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun computation(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun trampoline(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun main(): Scheduler {
        return Schedulers.trampoline()
    }
}