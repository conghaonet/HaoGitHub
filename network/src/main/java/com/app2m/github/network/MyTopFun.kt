package com.app2m.github.network

import android.app.Application
import android.content.Context
import okhttp3.Credentials

fun getBasicCredentials(username: String, password: String) = Credentials.basic(username, password)
