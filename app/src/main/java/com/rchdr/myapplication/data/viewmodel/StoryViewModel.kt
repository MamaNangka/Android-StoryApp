package com.rchdr.myapplication.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rchdr.myapplication.data.di.Injection
import com.rchdr.myapplication.data.paging.StoryRepository
import com.rchdr.myapplication.data.response.ListStoryItem

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun story(header: String): LiveData<PagingData<ListStoryItem>> = storyRepository.getStoryPaging(header).cachedIn(viewModelScope)

    class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(StoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StoryViewModel(Injection.provideRepository(context)) as T
            }
            else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}