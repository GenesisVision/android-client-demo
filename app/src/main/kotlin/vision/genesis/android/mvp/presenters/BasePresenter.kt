package vision.genesis.android.mvp.presenters

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<T> : MvpPresenter<T>() where T : MvpView {
    val compositeSubscription: CompositeDisposable = CompositeDisposable()

    protected fun unsubscribeOnDestroy(subscription: Disposable) {
        compositeSubscription.add(subscription)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.dispose()
    }
}
