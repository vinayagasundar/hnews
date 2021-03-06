package com.blackcatz.android.hnews.mvi.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blackcatz.android.hnews.mvi.MviIntent
import com.blackcatz.android.hnews.mvi.MviView
import com.blackcatz.android.hnews.mvi.MviViewModel
import com.blackcatz.android.hnews.mvi.MviViewState
import com.blackcatz.android.hnews.mvi.rx.AsyncTransformer
import com.blackcatz.android.hnews.mvi.rx.RxLifeCycle
import com.blackcatz.android.hnews.mvi.rx.RxTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * @author vinayagasundar
 */
abstract class MviAppActivity<I : MviIntent, S : MviViewState, out VM : MviViewModel<I, S>>
    : AppCompatActivity(),
    MviView<I, S>, RxLifeCycle {

    private val compositeDisposable = CompositeDisposable()
    private val viewModel: VM by lazy {
        createViewModel()
    }

    abstract fun createViewModel(): VM

    open fun injectDependencies() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        bind()
    }

    override fun onStop() {
        super.onStop()
        unBind()
    }

    private fun unBind() {
        compositeDisposable.clear()
    }

    private fun bind() {
        bind {
            viewModel.states()
                .compose(async())
                .subscribe(this::render, Timber::e)
        }
        viewModel.processIntents(intents())
    }

    final override fun bind(data: () -> Disposable) {
        compositeDisposable.add(data.invoke())
    }

    final override fun <T> async(): RxTransformer<T> = AsyncTransformer.create()
}