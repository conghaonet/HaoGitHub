package com.app2m.github.network

import com.app2m.github.network.data.Authorization
import com.app2m.github.network.data.AuthorizationBody
import com.app2m.github.network.data.GitHubApi
import com.app2m.github.network.data.UsersOwner
import io.reactivex.Observable
import retrofit2.http.*

interface GitHubService {
    @GET("/")
    fun getGitHubApi() : Observable<GitHubApi>

    @GET("users/{owner}")
    fun getUsersOwner(@Path("owner") owner : String) : Observable<UsersOwner>

    @POST("authorizations")
    fun postAuthorizations(@Body body : AuthorizationBody = AuthorizationBody()) : Observable<Authorization>
}