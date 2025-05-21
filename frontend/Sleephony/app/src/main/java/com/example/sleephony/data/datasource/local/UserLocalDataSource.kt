package com.example.sleephony.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.sleephony.data.model.user.UserProfileResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    val profileFlow: Flow<UserProfileResult> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            trySend(getProfile())
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(getProfile())  // 초기값 emit

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

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

    fun clearProfile() {
        prefs.edit {
            clear()
        }
    }

    fun updateProfileField(key: String, value: String) {
        prefs.edit {
            putString(key, value)
        }
    }
}