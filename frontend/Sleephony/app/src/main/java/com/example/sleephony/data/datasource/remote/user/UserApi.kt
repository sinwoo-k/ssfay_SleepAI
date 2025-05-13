package com.example.sleephony.data.datasource.remote.user

import com.example.sleephony.data.model.ApiResponse
import com.example.sleephony.data.model.user.UserProfileRequest
import com.example.sleephony.data.model.user.UserProfileResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface UserApi{
    // 회원 정보 조회
    @GET("user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") bearer: String
    ) : ApiResponse<UserProfileResult>

    // 회원 정보 수정
    @PATCH("user/profile")
    suspend fun patchUserProfile(
        @Header("Authorization") bearer: String,
        @Body req: UserProfileRequest
    )

    // 회원 탈퇴
    @DELETE("user/delete")
    suspend fun deleteUser(
        @Header("Authorization") bearer: String,
    )
}