package com.example.sleephony.data.datasource.local

import android.content.Context
import androidx.core.content.edit
import com.example.sleephony.data.model.user.UserProfileResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveProfile(profile: UserProfileResult) {
        prefs.edit {
            putString("email", profile.email)
            putString("nickname", profile.nickname)
            putString("birthData", profile.birthDate)
            putString("gender", profile.gender)
            putString("height", profile.height.toString())
            putString("weight", profile.weight.toString())
        }
    }

    fun getProfile(): UserProfileResult {
        val email = prefs.getString("email", "")
        val nickname = prefs.getString("nickname", "")
        val birthDate = prefs.getString("birthData", "")
        val gender    = prefs.getString("gender",    "")

        val heightStr = prefs.getString("height",  "0")
        val weightStr = prefs.getString("weight",  "0")

        val height = heightStr!!.toInt ()
        val weight = weightStr!!.toInt()

        return UserProfileResult(
            email     = email.toString(),
            nickname  = nickname.toString(),
            birthDate = birthDate.toString(),
            gender    = gender.toString(),
            height    = height,
            weight    = weight
        )
    }
}