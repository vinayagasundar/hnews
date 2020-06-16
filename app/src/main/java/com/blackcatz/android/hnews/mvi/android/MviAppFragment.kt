package com.blackcatz.android.hnews.mvi.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.blackcatz.android.hnews.mvi.MviIntent
import com.blackcatz.android.hnews.mvi.MviView
import com.blackcatz.android.hnews.mvi.MviViewModel
import com.blackcatz.android.hnews.mvi.MviViewState
import com.blackcatz.android.hnews.mvi.rx.RxLifeCycle
import com.blackcatz.android.hnews.mvi.rx.RxTransformer
import com.blackcatz.android.hnews.mvi.rx.AsyncTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class MviAppFragment<I : MviIntent, S : MviViewState, out VM : MviViewModel<I, S>>
    : Fragment(), MviView<I, S>, RxLifeCycle {


    private val compositeDisposable = CompositeDisposable()
    private val viewModel: VM by lazy {
        createViewModel()
    }

    abstract fun createViewModel(): VM

    open fun injectDependencies() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        bind()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        unBind()
        super.onDestroy()
    }

    private fun bind() {
        bind {
            viewModel.states()
                .compose(async())
                .subscribe(this::render, Timber::e)
        }
        viewModel.processIntents(intents())
    }

    private fun unBind() {
        compositeDisposable.clear()
    }

    final override fun bind(data: () -> Disposable) {
        compositeDisposable.add(data())
    }

    final override fun <T> async(): RxTransformer<T> = AsyncTransformer.create()
}