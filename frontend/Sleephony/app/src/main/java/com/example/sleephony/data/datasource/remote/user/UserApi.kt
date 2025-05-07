package com.example.sleephony.data.datasource.remote.user

import com.example.sleephony.data.model.ApiResponse
import com.example.sleephony.data.model.UserProfileResult
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi{
    // 회원 정보 조회
    @GET("user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") bearer: String
    ) : ApiResponse<UserProfileResult>
}