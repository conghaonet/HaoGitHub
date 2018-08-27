package com.app2m.github.network

import com.app2m.github.network.data.*
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.*

interface GitHubService {
    @GET("/")
    fun getGitHubApi() : Flowable<GitHubApi>

    @POST("authorizations")
    fun postAuthorizations(@Body body : AuthorizationBody = AuthorizationBody()) : Observable<Authorization>

    @GET("user")
    fun getUser() : Observable<User>

    @GET("users/{owner}")
    fun getUsersOwner(@Path("owner") owner : String) : Observable<UsersOwner>

}