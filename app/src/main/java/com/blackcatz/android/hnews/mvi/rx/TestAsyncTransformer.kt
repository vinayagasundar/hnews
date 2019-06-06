package com.blackcatz.android.hnews.mvi.rx

import io.reactivex.*
import org.reactivestreams.Publisher

class TestAsyncTransformer<T> : RxTransformer<T> {

    companion object {
        fun <T> create(): RxTransformer<T> {
            return TestAsyncTransformer()
        }
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream
    }

    override fun apply(upstream: Completable): CompletableSource {
        return upstream
    }
}