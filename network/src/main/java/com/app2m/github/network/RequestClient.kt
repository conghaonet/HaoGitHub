package com.app2m.github.network

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RequestClient {
    companion object {
        private val MY_HTTP_CLIENT by lazy {
            OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
//                    .addInterceptor(SlpRequestInterceptor())
                    .build()
        }

        /**
         * 使用默认的BASE_API构建retrofit实例
         */
        fun <T> buildService(serviceClass: Class<T>): T {
            return retrofit(BASE_API).create(serviceClass)
        }

        /**
         * 使用自定义的hosturl构建对应的retrofit实例
         *
         * @param baseApi 带http开头并且以'/'结束符的url地址
         */
        fun <T> buildService(baseApi: String, serviceClass: Class<T>): T {
            return retrofit(baseApi).create(serviceClass)
        }

        private fun retrofit(baseUrl: String): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(baseUrl)
//                .addConverterFactory(SlpGsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(MY_HTTP_CLIENT)
                    .build()
        }
    }
}