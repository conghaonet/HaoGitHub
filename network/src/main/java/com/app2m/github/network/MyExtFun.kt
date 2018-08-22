package com.app2m.github.network

import com.app2m.github.network.data.ErrResponse
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

fun<T> Observable<T>.schedule(subscribeOn : Scheduler = Schedulers.io(),
                              observeOn : Scheduler = AndroidSchedulers.mainThread()): Observable<T> {
    return subscribeOn(subscribeOn).observeOn(observeOn)
}

fun HttpException.getErrResponse() : ErrResponse? {
    var errResponse: ErrResponse? = null
    var response = response()
    response?.let {
        errResponse = ErrResponse(it.code(), it.message(), null)
        var responseBody = it.errorBody()
        var strBody = getBodyContent(responseBody)
        errResponse?.body = Gson().fromJson(strBody, ErrResponse.Body::class.java)
    }
    return errResponse
}