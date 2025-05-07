package com.example.sleephony.data.repository

import android.util.Log
import com.example.sleephony.data.datasource.local.UserLocalDataSource
import com.example.sleephony.data.datasource.remote.user.UserApi
import com.example.sleephony.data.model.UserProfileResult
import com.example.sleephony.domain.repository.UserRepository
import com.example.sleephony.utils.TokenProvider
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val tokenProvider: TokenProvider,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository{
    override suspend fun getUserProfile(): Result<UserProfileResult> =
        runCatching{
        val token = tokenProvider.getToken()
        val bearer = "Bearer $token"
        val response = api.getUserProfile(bearer)
        Log.d("DBG", "${response.results?.email}")
        userLocalDataSource.saveProfile(response.results)

        response.results
            ?: throw IllegalStateException("유저 정보를 찾을 수 없습니다.")
    }
}