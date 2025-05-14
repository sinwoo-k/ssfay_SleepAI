package com.example.sleephony.data.datasource.remote.report

import com.example.sleephony.data.model.ApiResponse
import com.example.sleephony.data.model.report.ReportResult
import com.example.sleephony.data.model.report.SleepDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ReportApi {

    @GET("sleep/report/detail/{date}")
    suspend fun getReportDetail(
        @Header("Authorization") bearer: String,
        @Path("date") date: String
    ): Response<ApiResponse<ReportResult>>

    @GET("sleep/report/graph/{date}")
    suspend fun getSleepGraph(
        @Header("Authorization") bearer: String,
        @Path("date") date: String
    ): Response<ApiResponse<List<SleepDataResponse>>>
}
