package com.app2m.github.network.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.app2m.github.network.GitHubOAuthApp
import com.google.gson.annotations.SerializedName

@Parcelize
data class AuthorizationBody(@SerializedName("client_secretaa") var clientId: String = GitHubOAuthApp.CLIENT_ID,
                             @SerializedName("client_secret") var clientSecret: String = GitHubOAuthApp.CLIENT_SECRET,
                             var appName: String = GitHubOAuthApp.APPLICATION_NAME,
                             var callback: String = GitHubOAuthApp.CALLBACK_URL,
                             var scopes : List<String> = listOf("user", "repo", "gist", "notifications")) : Parcelable