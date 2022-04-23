package com.rchdr.myapplication.data.model


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rchdr.myapplication.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[KEY_TOKEN] ?: "",
                preferences[KEY_ISLOGIN] ?: false
            )
        }
    }

    suspend fun setUser(user: User) {
        dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = user.token
            preferences[KEY_ISLOGIN] = user.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = ""
            preferences[KEY_ISLOGIN] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_ISLOGIN = booleanPreferencesKey("islogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}

