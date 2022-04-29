package com.rchdr.myapplication.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rchdr.myapplication.data.model.User
import com.rchdr.myapplication.data.model.UserPreference
import kotlinx.coroutines.launch

class AllViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser() : LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun setUser(user: User) {
        viewModelScope.launch {
            pref.setUser(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}