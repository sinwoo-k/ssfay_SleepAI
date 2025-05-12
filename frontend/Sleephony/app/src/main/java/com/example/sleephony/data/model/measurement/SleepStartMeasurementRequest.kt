package com.example.sleephony.data.model.measurement

import com.google.gson.annotations.SerializedName

data class SleepStartMeasurementRequest(
    @SerializedName("startedAt")
    val startedAt : String
)