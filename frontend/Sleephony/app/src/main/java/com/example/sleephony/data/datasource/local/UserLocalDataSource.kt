package com.example.sleephony.data.datasource.local

import android.content.Context
import androidx.core.content.edit
import com.example.sleephony.data.model.UserProfileResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveProfile(profile: UserProfileResult?) {
        prefs.edit {
            putString("email", profile?.email)
            putString("nickname", profile?.nickname)
            putString("birthData", profile?.birthDate)
            putString("gender", profile?.gender)
            putString("height", profile?.height.toString())
            putString("weight", profile?.weight.toString())
        }
    }

    fun getProfile(): UserProfileResult? {
        val email = prefs.getString("email", null) ?: return null
        val nickname = prefs.getString("nickname", "") ?: return null
        val birthDate = prefs.getString("birthData", null) ?: return null
        val gender    = prefs.getString("gender",    null) ?: return null

        val heightStr = prefs.getString("height",  null) ?: return null
        val weightStr = prefs.getString("weight",  null) ?: return null

        val height = heightStr.toIntOrNull() ?: return null
        val weight = weightStr.toIntOrNull() ?: return null

        return UserProfileResult(
            email     = email,
            nickname  = nickname,
            birthDate = birthDate,
            gender    = gender,
            height    = height,
            weight    = weight
        )
    }
}