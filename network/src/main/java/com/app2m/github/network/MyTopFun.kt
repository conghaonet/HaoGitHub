package com.app2m.github.network

import okhttp3.Credentials

fun getBasicCredentials(username: CharSequence, password:CharSequence) = Credentials.basic(username.toString().trim(), password.toString().trim())