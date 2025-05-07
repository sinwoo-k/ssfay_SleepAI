package com.example.sleephony.domain.repository

import com.example.sleephony.data.model.UserProfileResult

interface UserRepository {
    suspend fun getUserProfile(): Result<UserProfileResult>
}