package com.rchdr.myapplication.data.model

data class User(
    val userId: String,
    val name: String,
    val token: String,
    val isLogin: Boolean
)
