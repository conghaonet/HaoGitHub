package com.app2m.github.network

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.support.v4.content.ContextCompat
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import org.jetbrains.anko.AnkoContextImpl
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
class GitHubInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        //这一步是为了urlEncode，否则url包含特殊字符时会出错。
        val originalRequestUrl = HttpUrl.parse(originalRequest.url().toString())!!.uri().toString()
        val builder = originalRequest.newBuilder().url(originalRequestUrl)
        builder.addHeader("api-version", "application/vnd.github.v3+json")
        builder.addHeader("Content-Type", "application/json")
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
            AnkoLogger<GitHubInterceptor>().info("error code = ${response.code()}")
        }
        return response
    }

}