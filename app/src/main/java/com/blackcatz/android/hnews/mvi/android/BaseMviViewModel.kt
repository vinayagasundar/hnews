package com.blackcatz.android.hnews.mvi.android

import androidx.lifecycle.ViewModel
import com.blackcatz.android.hnews.mvi.MviAction
import com.blackcatz.android.hnews.mvi.MviIntent
import com.blackcatz.android.hnews.mvi.MviViewModel
import com.blackcatz.android.hnews.mvi.MviViewState
import com.blackcatz.android.hnews.mvi.rx.IRxBinder
import com.blackcatz.android.hnews.mvi.rx.RxTransformer
import com.blackcatz.android.hnews.mvi.rx.RxTransformerImpl
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

abstract class BaseMviViewModel<I : MviIntent, S : MviViewState, A : MviAction> : ViewModel(), MviViewModel<I, S>,
    IRxBinder {

    private val compositeDisposable = CompositeDisposable()

    /**
     * Proxy subject used to keep the stream alive even after the UI gets recycled.
     * This is basically used to keep ongoing events and the last cached State alive
     * while the UI disconnects and reconnects on config changes.
     */
    protected val intentsSubject: PublishSubject<I> = PublishSubject.create()
    private val statesObservable: Observable<S> by lazy { compose() }

    override fun states(): Observable<S> = statesObservable

    override fun processIntents(intents: Observable<I>) {
        intents.subscribe(intentsSubject)
    }

    /**
     * Process all the [MviIntent] and return the [MviViewState]
     */
    abstract fun compose(): Observable<S>

    /**
     * Get the [MviAction] for given [MviIntent]
     */
    abstract fun actionFromIntents(intents: I): A

    final override fun rxBind(data: () -> Disposable) {
        compositeDisposable.add(data.invoke())
    }

    final override fun <T> async(): RxTransformer<T> = RxTransformerImpl.create()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}