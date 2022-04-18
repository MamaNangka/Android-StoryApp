package com.rchdr.myapplication.data.response

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @field:SerializedName("userId")
    val userId: Boolean,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("token")
    val token: String
)
