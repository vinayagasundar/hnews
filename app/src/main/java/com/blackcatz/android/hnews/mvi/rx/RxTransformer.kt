package com.blackcatz.android.hnews.mvi.rx


import io.reactivex.*

interface RxTransformer<T> : ObservableTransformer<T, T>,
    FlowableTransformer<T, T>,
    SingleTransformer<T, T>,
    MaybeTransformer<T, T>,
    CompletableTransformer