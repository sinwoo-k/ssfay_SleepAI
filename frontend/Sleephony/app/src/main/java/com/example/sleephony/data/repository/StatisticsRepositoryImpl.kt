package com.example.sleephony.data.repository

import android.util.Log
import com.example.sleephony.data.datasource.remote.statistics.StatisticsApi
import com.example.sleephony.data.model.StatisticRequest
import com.example.sleephony.data.model.StatisticResponse
import com.example.sleephony.data.model.StatisticSummaryRequest
import com.example.sleephony.data.model.StatisticSummaryResponse
import com.example.sleephony.utils.TokenProvider
import com.kakao.sdk.auth.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatisticsRepositoryImpl @Inject constructor(
    private val api: StatisticsApi,
    private val tokenProvider: TokenProvider
) {

    suspend fun getStatistics(request: StatisticRequest): StatisticResponse {
        val token = tokenProvider.getToken()
        val request = request
        return api.getStatistics("Bearer $token",request)
    }

    suspend fun getStatisticsSummary(request: StatisticSummaryRequest): StatisticSummaryResponse {
        val token = tokenProvider.getToken()
        val request = request
        return api.getStatisticsSummary("Bearer $token",request)
    }
}
