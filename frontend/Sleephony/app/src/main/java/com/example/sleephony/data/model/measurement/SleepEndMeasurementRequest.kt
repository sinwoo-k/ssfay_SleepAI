package com.example.sleephony.data.model.measurement

import com.google.gson.annotations.SerializedName

data class SleepEndMeasurementRequest(
    @SerializedName("endedAt")
    val endedAt: String
)