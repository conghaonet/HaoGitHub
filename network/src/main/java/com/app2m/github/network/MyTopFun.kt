package com.app2m.github.network

import okhttp3.Credentials
import okhttp3.ResponseBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.nio.charset.Charset

fun getBasicCredentials(username: String, password: String) : String = Credentials.basic(username, password)
