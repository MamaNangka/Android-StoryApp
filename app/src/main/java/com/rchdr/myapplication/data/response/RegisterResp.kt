package com.rchdr.myapplication.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResp(

    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)


