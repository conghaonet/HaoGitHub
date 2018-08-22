package com.app2m.github.network

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.support.v4.content.ContextCompat
import com.app2m.github.network.data.ErrResponse
import com.google.gson.Gson
import okhttp3.*
import okhttp3.internal.http.HttpHeaders
import org.jetbrains.anko.AnkoContextImpl
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.nio.charset.Charset

class GitHubInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        //这一步是为了urlEncode，否则url包含特殊字符时会出错。
        val originalRequestUrl = HttpUrl.parse(originalRequest.url().toString())!!.uri().toString()
        val builder = originalRequest.newBuilder().url(originalRequestUrl)
        builder.addHeader("api-version", API_VERSION)
        builder.addHeader("Content-Type", CONTENT_TYPE)
        val prefAuthToken : String by Preference(GithubInit.getApplicationContext(), PrefProperty.AUTH_TOKEN, "")
        if(prefAuthToken.isEmpty()) {
            val prefAuthBasic : String by Preference(GithubInit.getApplicationContext(), PrefProperty.AUTH_BASIC, "")
            builder.addHeader("Authorization", prefAuthBasic)
        } else {
            builder.addHeader("Authorization", "token $prefAuthToken")
        }

        val request = builder.build()
        val response = chain.proceed(request)
        if(response.code() == 200) {
//            prefLoginSuccessful = true
        } else if (response.code() >= 400) {
            var errResponse = ErrResponse(response.code(), response.message(), null)
            if(!HttpHeaders.hasBody(response)) {
                //END HTTP
            } else if(bodyEncoded(response.headers())) {
                //HTTP (encoded body omitted)
            } else {
                val strBody = getBodyContent(response.body())
                errResponse.body = Gson().fromJson(strBody, ErrResponse.Body::class.java)
                AnkoLogger<GitHubInterceptor>().info("error body = $strBody")
            }
        }
        return response
    }
    private fun getBodyContent(responseBody: ResponseBody?): String? {
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
    private fun bodyEncoded(headers: Headers): Boolean {
        var contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && contentEncoding != "identity"
    }
}