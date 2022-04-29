package com.rchdr.myapplication.data.di

import android.content.Context
import com.rchdr.myapplication.api.RetrofitApiConfig
import com.rchdr.myapplication.data.database.StoryDatabase
import com.rchdr.myapplication.data.paging.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = RetrofitApiConfig.getApiService()

        return StoryRepository(database, apiService)
    }
}