package com.example.sleephony.data.repository

import android.app.Activity
import com.example.sleephony.data.datasource.remote.auth.KakaoAuthDataSource
import com.example.sleephony.domain.repository.AuthRepository
import com.kakao.sdk.auth.model.OAuthToken
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val kakao: KakaoAuthDataSource
) :AuthRepository {
    override suspend fun isLoggedIn(): Boolean {
        return false
    }

    override suspend fun loginWithKakao(activity: Activity): Result<OAuthToken> =
        runCatching { kakao.login(activity) }
}