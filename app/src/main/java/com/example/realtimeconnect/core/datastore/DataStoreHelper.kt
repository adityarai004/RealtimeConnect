package com.example.realtimeconnect.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.io.IOException
import javax.inject.Inject

class DataStoreHelper @Inject constructor(private val userPreferences: DataStore<Preferences>) {
    suspend fun setUserAuthKey(authKey: String) {
        try {
            userPreferences.edit { prefs ->
                prefs[stringPreferencesKey("authToken")] = authKey
                prefs[booleanPreferencesKey("isLoggedIn")] = true
            }
        } catch (ioException: IOException) {
            Log.e("TjPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun getUserAuthKey(): String {
        try {
            val preferences = userPreferences.data.first()
            return preferences[stringPreferencesKey("authToken")] ?: ""
        } catch (ioException: IOException) {
            Log.e("TjPreferences", "Failed to update user preferences", ioException)
            return ""
        }
    }
    suspend fun setIsLoggedIn(isLoggedIn: Boolean){
        try{
            userPreferences.edit {
                it[booleanPreferencesKey("isLoggedIn")] = isLoggedIn
            }
        } catch (ioException: IOException){
            Log.e("TjPreferences", "Failed to Update user data ${ioException.message}")
        }
    }

    suspend fun getIsLoggedIn(): Boolean {
        try{
            val prefs = userPreferences.data.first()
            return prefs[booleanPreferencesKey("isLoggedIn")] ?: false
        } catch (ioException: IOException){
            Log.e("TjPreferences", "Failed to Update user data ${ioException.message}")
        }
        return false
    }

    suspend fun storeString(key: String, value: String){
        try{
            userPreferences.edit {
                it[stringPreferencesKey(key)] = value
            }
        } catch (ioException: IOException){
            Log.e("TjPreferences", "Failed to Update user data ${ioException.message}")
        }
    }
    suspend fun getString(key: String): String{
        try{
            val prefs = userPreferences.data.first()
            return prefs[stringPreferencesKey(key)] ?: ""
        } catch (ioException: IOException){
            Log.e("TjPreferences", "Failed to Update user data ${ioException.message}")
        }
        return ""
    }

}