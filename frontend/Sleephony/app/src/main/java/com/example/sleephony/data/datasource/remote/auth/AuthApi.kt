package com.example.sleephony.data.datasource.remote.auth

import com.example.sleephony.data.model.*
import com.example.sleephony.data.model.auth.GoogleLoginRequest
import com.example.sleephony.data.model.auth.KakaoLoginRequest
import com.example.sleephony.data.model.auth.SocialLoginResult
import com.example.sleephony.data.model.auth.ValidateTokenResult
import com.example.sleephony.data.model.user.UserProfileRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {
    @POST("auth/login-kakao")
    suspend fun loginKakao(
        @Body req: KakaoLoginRequest
    ): ApiResponse<SocialLoginResult>

    @POST("auth/login-google")
    suspend fun loginGoogle(
        @Body req: GoogleLoginRequest
    ): ApiResponse<SocialLoginResult>

    @GET("auth/validate")
    suspend fun validateToken(
        @Header("Authorization") bearer: String
    ): ApiResponse<ValidateTokenResult>

    @PUT("user/profile")
    suspend fun submitProfile(
        @Header("Authorization") bearer: String,
        @Body req: UserProfileRequest
    ): ApiResponse<String>
}