package com.example.sleephony.domain.repository

import com.example.sleephony.data.model.user.UserProfileRequest
import com.example.sleephony.data.model.user.UserProfileResult

interface UserRepository {
    suspend fun getUserProfile(): Result<UserProfileResult>
    suspend fun patchUserProfile(req: UserProfileRequest): Result<String>
    suspend fun deleteUser(): Result<String>
}