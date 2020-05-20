package com.blackcatz.android.hnews.mvi.rx

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler

    fun computation(): Scheduler

    fun trampoline(): Scheduler

    fun main(): Scheduler
}