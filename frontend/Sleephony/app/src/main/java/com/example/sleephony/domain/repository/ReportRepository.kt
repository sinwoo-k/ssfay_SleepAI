package com.example.sleephony.domain.repository

import com.example.sleephony.data.model.report.AiReportResponse
import com.example.sleephony.data.model.report.ReportResponse
import com.example.sleephony.data.model.report.SleepDataResponse

interface ReportRepository {
    suspend fun getReportDetail(date : String) :Result<ReportResponse>
    suspend fun getSleepGraph(date: String): Result<List<SleepDataResponse>>
    suspend fun getReportDetailed(date: String): Result<AiReportResponse>
    suspend fun getAiReport(date: String): Result<String>
    suspend fun getReportDates(month: String): Result<List<String>>
}