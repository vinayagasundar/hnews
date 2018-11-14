package com.blackcatz.android.hnews.mvi.android

import androidx.appcompat.app.AppCompatActivity
import com.blackcatz.android.hnews.mvi.MviIntent
import com.blackcatz.android.hnews.mvi.MviView
import com.blackcatz.android.hnews.mvi.MviViewModel
import com.blackcatz.android.hnews.mvi.MviViewState
import com.blackcatz.android.hnews.mvi.rx.IRxBinder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author vinayagasundar
 */
abstract class MvpAppActivity<I : MviIntent, S : MviViewState, out VM : MviViewModel<I, S>>
    : AppCompatActivity(),
    MviView<I, S>, IRxBinder {

    protected val compositeDisposable = CompositeDisposable()
    protected val viewModel: VM by lazy {
        createViewModel()
    }

    abstract fun createViewModel(): VM

    override fun onStart() {
        super.onStart()
        bind()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun bind() {
        rxBind {
            viewModel.states().subscribe(this::render)
        }
        viewModel.processIntents(intents())
    }

    final override fun rxBind(data: () -> Disposable) {
        compositeDisposable.add(data.invoke())
    }
}