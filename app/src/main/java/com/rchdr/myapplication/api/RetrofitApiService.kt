package com.rchdr.myapplication.api

import com.rchdr.myapplication.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitApiService {

    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResp>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResp>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<StoryResp>

    @GET("stories")
    fun getStoryLocation(
        @Header("Authorization") authorization: String,
        @Query("location") includeLocation: Int = 1
    ): Call<StoryResp>

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") header: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : StoryResp

    @Multipart
    @POST("stories")
    fun postStoryLocation(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float
    ) : Call<StoryResp>


}