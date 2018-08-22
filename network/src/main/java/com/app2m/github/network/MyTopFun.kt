package com.app2m.github.network

import okhttp3.Credentials
import okhttp3.ResponseBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.nio.charset.Charset

fun getBasicCredentials(username: String, password: String) : String = Credentials.basic(username, password)

fun getBodyContent(responseBody: ResponseBody?): String? {
    var strBody: String? = null
    responseBody?.let {
        it.source()?.use {buffered ->
            //Buffer the entire body.
            buffered.request(java.lang.Long.MAX_VALUE)
            val buffer = buffered.buffer()
            var charset = Charset.forName(DEFAULT_CHARSET)
            it.contentType()?.let { mediaType ->
                charset = mediaType.charset(Charset.forName(DEFAULT_CHARSET))
                if(REQUEST_MEDIA_TYPE == mediaType.type() && REQUEST_MEDIA_SUB_TYPE == mediaType.subtype()) {
                    AnkoLogger<GitHubInterceptor>().info("MediaType is $mediaType")
                }
            }
            if (it.contentLength() > 0) {
                strBody = buffer.clone().readString(charset)
            }
        }
    }
    return strBody
}