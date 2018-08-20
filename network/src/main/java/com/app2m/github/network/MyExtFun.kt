package com.app2m.github.network

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun<T> Observable<T>.schedule(subscribeOn : Scheduler = Schedulers.io(),
                              observeOn : Scheduler = AndroidSchedulers.mainThread()): Observable<T> {
    return subscribeOn(subscribeOn).observeOn(observeOn)
}