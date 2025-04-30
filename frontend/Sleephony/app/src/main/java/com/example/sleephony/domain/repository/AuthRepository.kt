package com.example.sleephony.domain.repository

import android.app.Activity
import com.kakao.sdk.auth.model.OAuthToken

interface AuthRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun loginWithKakao(activity: Activity): Result<OAuthToken>
}