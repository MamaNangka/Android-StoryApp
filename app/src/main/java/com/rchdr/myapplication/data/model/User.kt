package com.rchdr.myapplication.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val token: String,
    val isLogin: Boolean
) : Parcelable
