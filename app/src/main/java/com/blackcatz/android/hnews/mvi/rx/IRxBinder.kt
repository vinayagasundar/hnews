package com.blackcatz.android.hnews.mvi.rx

import io.reactivex.disposables.Disposable

interface IRxBinder {
    fun rxBind(data: () -> Disposable)
}