package com.app2m.github.network

import com.app2m.github.network.data.GitHubApi
import com.app2m.github.network.data.UsersOwner
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("/")
    fun getGitHubApi() : Observable<GitHubApi>

    @GET("users/{owner}")
    fun getUsersOwner(@Path("owner") owner : String) : Observable<UsersOwner>
}