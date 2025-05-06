package com.example.sleephony.data.repository

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.sleephony.data.datasource.GoogleAuthDataSource
import com.example.sleephony.data.datasource.remote.auth.AuthApi
import com.example.sleephony.data.datasource.remote.auth.KakaoAuthDataSource
import com.example.sleephony.data.model.GoogleLoginRequest
import com.example.sleephony.data.model.KakaoLoginRequest
import com.example.sleephony.data.model.SocialLoginResult
import com.example.sleephony.data.model.UserProfileRequest
import com.example.sleephony.domain.repository.AuthRepository
import com.example.sleephony.utils.TokenProvider
import com.kakao.sdk.auth.model.OAuthToken
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val kakao: KakaoAuthDataSource,
    private val google: GoogleAuthDataSource,
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider
) :AuthRepository {
    override suspend fun isLoggedIn(): Boolean {
        val token = tokenProvider.getToken()
        Log.d("DBG", "토큰 검사 결과 $token")
        if (token?.isBlank() == true) {
            return false
        }
        return try {
            val bearer = "Bearer $token"
            val resp = authApi.validateToken(bearer)   // 또는 검증 전용 API
            resp.code == "SU"
        } catch (e: Exception) {
            Log.e("DBG", "검사 결과 $e")
            false
        }
    }

    override suspend fun loginWithKakao(activity: Activity): Result<SocialLoginResult> =
        runCatching {
            // 1) SDK 로그인
            val sdk = kakao.login(activity).accessToken
            // 2) 백엔드 호출
            val resp = authApi.loginKakao(KakaoLoginRequest(accessToken = sdk))
            if (resp.code != "SU") {
                throw RuntimeException(resp.message)
            }
            val result = resp.results
                ?: throw RuntimeException("로그인 결과가 없습니다.")
            // 3) JWT 저장
            tokenProvider.saveToken(result.accessToken)
            result
        }

    override suspend fun loginWithGoogle(activity: Activity): Result<SocialLoginResult> =
        runCatching {
            val email = google.signIn(activity)
            val resp = authApi.loginGoogle(GoogleLoginRequest(email = email.toString()))
            if (resp.code != "SU") {
                throw RuntimeException(resp.message)
            }
            val result = resp.results
                ?: throw RuntimeException("로그인 결과가 없습니다.")
            tokenProvider.saveToken(result.accessToken)
            result
        }

    override suspend fun submitProfile(request: UserProfileRequest) = runCatching {
        val token = tokenProvider.getToken()
        val bearer = "Bearer $token"

        val resp = authApi.submitProfile(bearer, request)

        if (resp.code != "SU") {
            throw RuntimeException("프로필 저장 실패: ${resp.message}")
        }
    }.onFailure { throwable ->
        if (throwable is HttpException) {
            val errBody = throwable.response()?.errorBody()?.string()
            Log.e("ProfileSetup", "HTTP ${throwable.code()} 에러 바디: $errBody")
        } else {
            Log.e("ProfileSetup", "프로필 저장 중 예외", throwable)
        }
    }
}