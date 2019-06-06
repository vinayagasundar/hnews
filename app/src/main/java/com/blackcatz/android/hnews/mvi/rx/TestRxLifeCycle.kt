package com.blackcatz.android.hnews.mvi.rx

import io.reactivex.disposables.Disposable

class TestRxLifeCycle : RxLifeCycle {

    override fun bind(data: () -> Disposable) {
        // TODO : Nothing for now
    }

    override fun <T> async(): RxTransformer<T> {
        return TestAsyncTransformer.create()
    }
}