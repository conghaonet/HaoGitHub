package com.app2m.github.network

import android.app.Application
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
class GitHubInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        //这一步是为了urlEncode，否则url包含特殊字符时会出错。
        val originalRequestUrl = HttpUrl.parse(originalRequest.url().toString())!!.uri().toString()
        val builder = originalRequest.newBuilder().url(originalRequestUrl)

        var prefLoginSuccessful : Boolean by Preference(Application().applicationContext, PrefProperty.LOGIN_SUCCESSFUL, false)
        if(prefLoginSuccessful) {
           val prefAuthBasic : String by Preference(Application().applicationContext, PrefProperty.AUTH_BASIC, "")
            builder.addHeader("Authorization", prefAuthBasic)
        }
        val request = builder.build()
        val response = chain.proceed(request)
        if(response.code() == 200) {
            prefLoginSuccessful = true
        } else if (response.code() >= 400) {
            AnkoLogger<GitHubInterceptor>().info("error code = ${response.code()}")
        }
        return response
    }

}