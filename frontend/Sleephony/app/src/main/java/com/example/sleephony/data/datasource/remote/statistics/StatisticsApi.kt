package com.example.sleephony.data.datasource.remote.statistics

import android.util.Log
import androidx.transition.Visibility.Mode
import com.example.sleephony.data.model.StatisticRequest
import com.example.sleephony.data.model.StatisticResponse
import com.example.sleephony.data.model.StatisticSummaryRequest
import com.example.sleephony.data.model.StatisticSummaryResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import javax.inject.Singleton

interface StatisticsApi  {

    @POST("sleep/stat/graph")
    suspend fun getStatistics (
        @Header("Authorization") bearer: String,
        @Body request: StatisticRequest
    ): StatisticResponse

    @POST("sleep/stat/summary")
    suspend fun getStatisticsSummary(
        @Header("Authorization") bearer: String,
        @Body request: StatisticSummaryRequest
    ): StatisticSummaryResponse
}

@Module
@InstallIn(SingletonComponent::class)
object StatisticsModule {

    @Provides
    @Singleton
    fun provideStatisticsApi(retrofit: Retrofit): StatisticsApi {
        return retrofit.create(StatisticsApi::class.java)
    }
}