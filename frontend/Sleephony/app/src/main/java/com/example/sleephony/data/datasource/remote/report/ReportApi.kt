package com.example.sleephony.data.datasource.remote.report

import com.example.sleephony.data.model.ApiResponse
import com.example.sleephony.data.model.report.AiReportResponse
import com.example.sleephony.data.model.report.ReportResponse
import com.example.sleephony.data.model.report.SleepDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ReportApi {

    @GET("sleep/reports/dates/{month}")
    suspend fun getReportDates(
        @Header("Authorization") bearer: String,
        @Path("month") month: String
    ): Response<ApiResponse<List<String>>>

    @GET("sleep/report/detail/{date}")
    suspend fun getReportDetail(
        @Header("Authorization") bearer: String,
        @Path("date") date: String
    ): Response<ApiResponse<ReportResponse>>

    @GET("sleep/report/graph/{date}")
    suspend fun getSleepGraph(
        @Header("Authorization") bearer: String,
        @Path("date") date: String
    ): Response<ApiResponse<List<SleepDataResponse>>>

    @GET("sleep/report/detailed/{date}")
    suspend fun getReportDetailed(
        @Header("Authorization") bearer: String,
        @Path("date") date: String
    ): Response<ApiResponse<AiReportResponse>>

    @GET("sleep/ai-report/{date}")
    suspend fun getAiReport(
        @Header("Authorization") bearer: String,
        @Path("date") date: String
    ): Response<ApiResponse<String>>
}
