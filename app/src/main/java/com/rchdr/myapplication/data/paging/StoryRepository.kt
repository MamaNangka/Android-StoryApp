package com.rchdr.myapplication.data.paging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rchdr.myapplication.api.RetrofitApiService
import com.rchdr.myapplication.data.database.StoryDatabase
import com.rchdr.myapplication.data.response.ListStoryItem

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: RetrofitApiService) {
    fun getStoryPaging(header: String) : LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, header)
            }
        ).liveData
    }
}