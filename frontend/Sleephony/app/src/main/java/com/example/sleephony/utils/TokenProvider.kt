package com.example.sleephony.utils

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class TokenProvider  @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit() { putString("accessToken", token) }
    }

    fun getToken(): String? =
        prefs.getString("accessToken", null)

    fun clearToken(token: String) {
        prefs.edit() { remove("accessToken").apply() }
    }

}