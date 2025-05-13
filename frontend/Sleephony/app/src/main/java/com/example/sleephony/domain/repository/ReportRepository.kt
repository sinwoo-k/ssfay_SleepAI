package com.example.sleephony.domain.repository

import com.example.sleephony.data.model.report.ReportResult

interface ReportRepository {
    suspend fun getReportDetail(date : String) :Result<ReportResult>
}