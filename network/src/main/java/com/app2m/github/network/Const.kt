package com.app2m.github.network

import android.annotation.SuppressLint
import android.content.Context

const val BASE_API = "https://api.github.com/"
const val API_VERSION = "application/vnd.github.v3+json"
const val REQUEST_MEDIA_TYPE = "application"
const val REQUEST_MEDIA_SUB_TYPE = "json"
const val CONTENT_TYPE = "$REQUEST_MEDIA_TYPE/$REQUEST_MEDIA_SUB_TYPE"
const val DEFAULT_CHARSET = "UTF-8"

object GitHubOAuthApp {
    const val CLIENT_ID  = "c868cf1dc9c48103bb55"
    const val CLIENT_SECRET  = "b341fe1eb154f1d78b4f8f58288106d95bce3bf0"
    const val APPLICATION_NAME = "HaoGitHub"
    const val CALLBACK_URL = "https://github.com/conghaonet/HaoGitHub/callback"
}

object PrefProperty {
    const val USERNAME = "username"
    const val PASSWORD = "password"
    const val AUTH_BASIC = "authentication_basic"
    const val AUTH_TOKEN = "authentication_token"
    const val LOGIN_SUCCESSFUL = "login_successful"
}

@SuppressLint("StaticFieldLeak")
object GithubInit {
    private var context: Context? = null
    fun getApplicationContext() : Context {
        if (context == null) {
            throw NullPointerException("Please execute GithubInit.init(context: Context) at first!")
        } else {
            return context!!
        }
    }
    fun init(context: Context) {
        this.context = context.applicationContext
    }


}