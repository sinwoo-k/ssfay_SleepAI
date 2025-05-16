package com.example.sleephony.data.model.measurement

import com.google.gson.annotations.SerializedName

data class SleepEndMeasurementResult(
    @SerializedName("sleepReportId") val sleepReportId: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("sleepScore") val sleepScore: Int,
    @SerializedName("sleepTime") val sleepTime: String,
    @SerializedName("realSleepTime") val realSleepTime: String?,
    @SerializedName("sleepWakeTime") val sleepWakeTime: String,
    @SerializedName("awakeTime") val awakeTime: Int,
    @SerializedName("remTime") val remTime: Int,
    @SerializedName("nremTime") val nremTime: Int,
    @SerializedName("deepTime") val deepTime: Int,
    @SerializedName("sleepCycle") val sleepCycle: Int,
    @SerializedName("createdAt") val createdAt: String
)