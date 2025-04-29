package com.example.noteapp.data.data_source.local.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorageUtils @Inject constructor (private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val THEME_KEY = booleanPreferencesKey("theme_dark_mode")
    }

    // Lưu theme (dark/light)
    suspend fun saveTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_KEY] = isDarkTheme
        }
    }

    // Đọc theme
    val themeFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.THEME_KEY] ?: false // Default là false (light theme)
        }
}
