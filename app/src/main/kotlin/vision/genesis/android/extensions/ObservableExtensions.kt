package vision.genesis.android.extensions

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.subscribeOnIoThread() = this.subscribeOn(Schedulers.io())

fun <T> Observable<T>.observeOnMainThread() = this.observeOn(AndroidSchedulers.mainThread())
