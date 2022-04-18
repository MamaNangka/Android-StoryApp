package com.k6genap.githubuserapp.api

import com.k6genap.githubuserapp.data.model.response.DetailUserResp
import com.k6genap.githubuserapp.data.model.User
import com.k6genap.githubuserapp.data.model.response.UserResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query:String
    ): Call<UserResp>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username:String
    ): Call<DetailUserResp>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username:String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username:String
    ): Call<ArrayList<User>>
}