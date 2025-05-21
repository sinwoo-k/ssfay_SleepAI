package com.example.sleephony.data.repository

import com.example.sleephony.BuildConfig
import com.example.sleephony.data.datasource.remote.report.ReportApi
import com.example.sleephony.data.model.ApiResponse
import com.example.sleephony.data.model.report.AiReportResponse
import com.example.sleephony.data.model.report.ReportResponse
import com.example.sleephony.data.model.report.SleepDataResponse
import com.example.sleephony.domain.repository.ReportRepository
import com.example.sleephony.utils.TokenProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val tokenProvider: TokenProvider
) : ReportRepository {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SLEEPHONY_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val reportApi: ReportApi = retrofit.create(ReportApi::class.java)

    override suspend fun getReportDetail(date: String): Result<ReportResponse> =
        runCatching {
            val token = tokenProvider.getToken()
            val bearer = "Bearer $token"
            val response: Response<ApiResponse<ReportResponse>> = reportApi.getReportDetail(bearer, date)

            if (response.isSuccessful) {
                response.body()?.results ?: throw RuntimeException("수면 리포트 정보가 없습니다.")
            } else {
                throw RuntimeException("HTTP 오류: ${response.code()} ${response.message()}")
            }
        }

    override suspend fun getSleepGraph(date: String): Result<List<SleepDataResponse>> =
        runCatching {
            val token = tokenProvider.getToken()
            val bearer = "Bearer $token"
            val response: Response<ApiResponse<List<SleepDataResponse>>> = reportApi.getSleepGraph(bearer, date)

            if (response.isSuccessful) {
                response.body()?.results ?: throw RuntimeException("수면 그래프 정보가 없습니다.")
            } else {
                throw RuntimeException("HTTP 오류: ${response.code()} ${response.message()}")
            }
        }

    override suspend fun getReportDetailed(date: String): Result<AiReportResponse> =
        runCatching {
            val token = tokenProvider.getToken()
            val bearer = "Bearer $token"
            val response = reportApi.getReportDetailed(bearer, date)

            if (response.isSuccessful) {
                response.body()?.results ?: throw RuntimeException("AI 리포트 상세 정보가 없습니다.")
            } else {
                throw RuntimeException("HTTP 오류: ${response.code()} ${response.message()}")
            }
        }

    override suspend fun getAiReport(date: String): Result<String> =
        runCatching {
            val token = tokenProvider.getToken()
            val bearer = "Bearer $token"
            val response = reportApi.getAiReport(bearer, date)

            if (response.isSuccessful) {
                response.body()?.results ?: throw RuntimeException("AI 분석 결과가 없습니다.")
            } else {
                throw RuntimeException("HTTP 오류: ${response.code()} ${response.message()}")
            }
        }

    override suspend fun getReportDates(month: String): Result<List<String>> =
        runCatching {
            val token = tokenProvider.getToken()
            val bearer = "Bearer $token"
            val response = reportApi.getReportDates(bearer, month)

            if (response.isSuccessful) {
                response.body()?.results ?: throw RuntimeException("수면 기록 날짜가 없습니다.")
            } else {
                throw RuntimeException("HTTP 오류: ${response.code()} ${response.message()}")
            }
        }
}
