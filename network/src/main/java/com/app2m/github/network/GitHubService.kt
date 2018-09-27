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

    /**
     * current_user_url=https://api.github.com/user
     */
    @GET("user")
    fun getCurrentUser() : Observable<User>

    @GET("users/{user}")
    fun getUser(@Path("user") user : String) : Observable<User>

}