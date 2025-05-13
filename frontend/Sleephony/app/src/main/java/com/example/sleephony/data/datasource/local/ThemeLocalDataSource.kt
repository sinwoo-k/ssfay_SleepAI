package com.example.sleephony.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "theme_prefs"
)

@Singleton
class ThemeLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val THEME_ID_KEY = intPreferencesKey("theme_id")
        private const val DEFAULT_THEME_ID = 1
    }

    private val dataStore = context.themeDataStore

    suspend fun saveThemeId(id: Int) {
        dataStore.edit { prefs ->
            prefs[THEME_ID_KEY] = id
        }
    }

    val themeIdFlow: Flow<Int> = dataStore.data
        .map { prefs ->
            prefs[THEME_ID_KEY] ?: DEFAULT_THEME_ID
        }
}