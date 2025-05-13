package com.example.sleephony.data.datasource.remote.report

import com.example.sleephony.data.model.ApiResponse
import com.example.sleephony.data.model.report.ReportResult
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
}
