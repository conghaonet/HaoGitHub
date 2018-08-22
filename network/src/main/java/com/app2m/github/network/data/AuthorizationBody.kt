package com.app2m.github.network.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.app2m.github.network.GitHubOAuthApp

@Parcelize
data class AuthorizationBody(var client_id: String = GitHubOAuthApp.CLIENT_ID,
                        var client_secret: String = GitHubOAuthApp.CLIENT_SECRET,
                        var appName: String = GitHubOAuthApp.APPLICATION_NAME,
                        var callback: String = GitHubOAuthApp.CALLBACK_URL,
                        var scopes : List<String> = listOf("user", "repo", "gist", "notifications")) : Parcelable