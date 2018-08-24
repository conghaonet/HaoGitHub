package com.app2m.github.network

import com.app2m.github.network.data.ErrResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.HttpException
import java.nio.charset.Charset

fun<T> Observable<T>.schedule(subscribeOn : Scheduler = Schedulers.io(),
                              observeOn : Scheduler = AndroidSchedulers.mainThread()): Observable<T> {
    return subscribeOn(subscribeOn).observeOn(observeOn)
}

fun HttpException.getErrResponse() : ErrResponse? {
    var errResponse: ErrResponse? = null
    val response = response()
    response?.let {
        errResponse = ErrResponse(it.code(), it.message(), null)
        val responseBody = it.errorBody()
        responseBody?.getContent()?.let {str ->
            try {
                errResponse?.body = Gson().fromJson(str, ErrResponse.Body::class.java)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }
        }
    }
    return errResponse
}

fun ResponseBody.getContent(): String? {
    var strBody: String? = null
    source()?.let {
        //Buffer the entire body.
        it.request(java.lang.Long.MAX_VALUE)
        val buffer = it.buffer()
        var charset = Charset.forName(DEFAULT_CHARSET)
        contentType()?.let { mediaType ->
            charset = mediaType.charset(Charset.forName(DEFAULT_CHARSET))
            if(REQUEST_MEDIA_TYPE == mediaType.type() && REQUEST_MEDIA_SUB_TYPE == mediaType.subtype()) {
                AnkoLogger<ResponseBody>().info("MediaType is $mediaType")
            }
        }
        if (contentLength() > 0) {
            strBody = buffer.clone().readString(charset)
        }
    }
    return strBody
}