package com.example.sleephony.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.settingDataStore: DataStore<Preferences> by preferencesDataStore(

    name = "user_settings"
)

@Singleton
class SettingsLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.settingDataStore

    // 다운로드 허용 여부
    companion object {
        private val ALLOW_MOBILE_DOWNLOAD_KEY = booleanPreferencesKey("allow_mobile_download")
    }

    suspend fun saveAllowMobileDownload(allow: Boolean) {
        dataStore.edit { prefs ->
            prefs[ALLOW_MOBILE_DOWNLOAD_KEY] = allow
        }
    }

    val allowMobileDownloadFlow: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[ALLOW_MOBILE_DOWNLOAD_KEY] ?: false }
}
