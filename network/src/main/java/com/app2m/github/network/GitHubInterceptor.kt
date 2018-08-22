package com.app2m.github.network

import com.app2m.github.network.data.ErrResponse
import com.google.gson.Gson
import okhttp3.*
import okhttp3.internal.http.HttpHeaders

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
//                AnkoLogger<GitHubInterceptor>().info("error body = $strBody")
            }
        }
        return response
    }
    private fun bodyEncoded(headers: Headers): Boolean {
        var contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && contentEncoding != "identity"
    }
}