package com.blackcatz.android.hnews.mvi.rx

import io.reactivex.disposables.Disposable

interface RxLifeCycle {
    fun bind(data: () -> Disposable)

    fun <T> async(): RxTransformer<T>
}