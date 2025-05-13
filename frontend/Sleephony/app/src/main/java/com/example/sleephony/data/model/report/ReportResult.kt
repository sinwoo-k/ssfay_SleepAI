package com.example.sleephony.data.model.report

import com.google.gson.annotations.SerializedName

data class ReportResult(
    @SerializedName("todayReport")
    val today: SleepReport,

    @SerializedName("previousTotalSleepMinutes")
    val previousTotalSleepMinutes: Int
)

