package com.app2m.github.network

import android.app.Application
import android.content.Context
import com.app2m.github.network.data.ErrResponse
import com.google.gson.Gson
import okhttp3.Credentials
import okhttp3.ResponseBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.HttpException
import java.nio.charset.Charset

fun getBasicCredentials(username: String, password: String) = Credentials.basic(username, password)

fun getErrResponse(throwable: Throwable): ErrResponse? {
    var errResponse: ErrResponse? = null
    if (throwable is HttpException) {
        var response = throwable.response()
        response?.let {
            errResponse = ErrResponse(it.code(), it.message(), null)
            var responseBody = it.errorBody()
            var strBody = getBodyContent(responseBody)
            errResponse?.body = Gson().fromJson(strBody, ErrResponse.Body::class.java)
        }
    }
    return errResponse

}
fun getBodyContent(responseBody: ResponseBody?): String? {
    var strBody: String? = null
    val source = responseBody?.source()
    //Buffer the entire body.
    source?.request(java.lang.Long.MAX_VALUE)
    val buffer = source?.buffer()
    val charset = Charset.forName("UTF-8")
    val contentType = responseBody?.contentType()
    if (contentType != null && REQUEST_MEDIA_TYPE ==contentType.type() && REQUEST_MEDIA_SUB_TYPE ==contentType.subtype()) {
        AnkoLogger<GitHubInterceptor>().info("error code = $contentType")
    }
    if (responseBody?.contentLength() != 0L) {
        strBody = buffer?.clone()?.readString(charset)
    }
    return strBody
}