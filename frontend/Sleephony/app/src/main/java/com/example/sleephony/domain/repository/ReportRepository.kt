package com.example.sleephony.domain.repository

import com.example.sleephony.data.model.report.ReportResult
import com.example.sleephony.data.model.report.SleepDataResponse

interface ReportRepository {
    suspend fun getReportDetail(date : String) :Result<ReportResult>
    suspend fun getSleepGraph(date: String): Result<List<SleepDataResponse>>
}