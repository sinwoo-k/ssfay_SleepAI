package com.example.sleephony.data.model.report

import com.google.gson.annotations.SerializedName

data class ReportResponse(
    @SerializedName("todayReport")
    val today: SleepReportResponse,

    @SerializedName("previousTotalSleepMinutes")
    val previousTotalSleepMinutes: Int
)

