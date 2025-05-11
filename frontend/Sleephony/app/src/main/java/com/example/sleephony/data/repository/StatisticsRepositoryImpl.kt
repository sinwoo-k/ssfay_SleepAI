package com.example.sleephony.data.repository

import com.example.sleephony.data.datasource.remote.statistics.StatisticsApi
import com.example.sleephony.data.model.StatisticRequest
import com.example.sleephony.data.model.StatisticResponse
import com.kakao.sdk.auth.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatisticsRepositoryImpl @Inject constructor(
    private val api: StatisticsApi,
    private val tokenManager: TokenManager
) {

    suspend fun getStatistics(request: StatisticRequest): StatisticResponse {
        val token = tokenManager.getToken()
        return api.getStatistics("Bear $token",request)
    }
}
