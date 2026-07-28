package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppPreferences(private val context: Context) {
    private val DEVICE_MAC = stringPreferencesKey("device_mac")
    private val DEVICE_PASSWORD = stringPreferencesKey("device_password")

    val deviceMac: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[DEVICE_MAC]
    }

    val devicePassword: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[DEVICE_PASSWORD]
    }

    suspend fun saveDeviceMac(mac: String) {
        context.dataStore.edit { preferences ->
            preferences[DEVICE_MAC] = mac
        }
    }

    suspend fun saveDevicePassword(password: String) {
        context.dataStore.edit { preferences ->
            preferences[DEVICE_PASSWORD] = password
        }
    }

    suspend fun clearDeviceMac() {
        context.dataStore.edit { preferences ->
            preferences.remove(DEVICE_MAC)
        }
    }
}
