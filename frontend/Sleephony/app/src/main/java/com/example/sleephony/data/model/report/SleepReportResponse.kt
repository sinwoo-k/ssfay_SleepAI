package com.example.sleephony.data.model.report

import com.google.gson.annotations.SerializedName

data class SleepReportResponse(
    @SerializedName("sleepReportId") val reportId: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("sleepScore") val sleepScore: Int,
    @SerializedName("sleepTime") val sleepStartTime: String,
    @SerializedName("realSleepTime") val realSleepDuration: String,
    @SerializedName("sleepWakeTime") val sleepEndTime: String,
    @SerializedName("awakeTime") val awakeMinutes: Int,
    @SerializedName("remTime") val remMinutes: Int,
    @SerializedName("nremTime") val lightMinutes: Int,   // nremTime 전체로 light로 저장
    @SerializedName("deepTime") val deepMinutes: Int,
    @SerializedName("sleepCycle") val sleepCycles: Int,
    @SerializedName("createdAt") val createdAt: String
)
