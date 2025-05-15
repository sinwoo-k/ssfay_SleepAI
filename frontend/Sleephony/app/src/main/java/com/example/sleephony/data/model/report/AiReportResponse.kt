package com.example.sleephony.data.model.report

import com.google.gson.annotations.SerializedName

data class AiReportResponse(
    @SerializedName("sleepScore") val sleepScore: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("sleepStart") val sleepStart: String,
    @SerializedName("sleepEnd") val sleepEnd: String,
    @SerializedName("totalSleepMinutes") val totalSleepMinutes: Int,
    @SerializedName("sleepLatencyMinutes") val sleepLatencyMinutes: Int,
    @SerializedName("deepSleepMinutes") val deepSleepMinutes: Int,
    @SerializedName("remSleepMinutes") val remSleepMinutes: Int,
    @SerializedName("statistics") val statistics: SleepStatistics
)

data class SleepStatistics(
    @SerializedName("avgTotalSleepMinutes") val avgTotalSleepMinutes: Int,
    @SerializedName("avgDeepSleepMinutes") val avgDeepSleepMinutes: Int,
    @SerializedName("avgRemSleepMinutes") val avgRemSleepMinutes: Int
)
