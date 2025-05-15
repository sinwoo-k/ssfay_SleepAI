package com.example.sleephony.data.repository

import com.example.sleephony.data.datasource.remote.report.ReportApi
import com.example.sleephony.data.model.ApiResponse
import com.example.sleephony.data.model.report.AiReportResponse
import com.example.sleephony.data.model.report.ReportResponse
import com.example.sleephony.data.model.report.SleepDataResponse
import com.example.sleephony.domain.repository.ReportRepository
import com.example.sleephony.utils.TokenProvider
import retrofit2.Response
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val api: ReportApi,
    private val tokenProvider: TokenProvider
) : ReportRepository {

    override suspend fun getReportDetail(date: String): Result<ReportResponse> =
        runCatching {
            val token = tokenProvider.getToken()
            val bearer = "Bearer $token"
            val response: Response<ApiResponse<ReportResponse>> = api.getReportDetail(bearer, date)

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
            val response: Response<ApiResponse<List<SleepDataResponse>>> = api.getSleepGraph(bearer, date)

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
            val response = api.getReportDetailed(bearer, date)

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
            val response = api.getAiReport(bearer, date)

            if (response.isSuccessful) {
                response.body()?.results ?: throw RuntimeException("AI 분석 결과가 없습니다.")
            } else {
                throw RuntimeException("HTTP 오류: ${response.code()} ${response.message()}")
            }
        }
}
