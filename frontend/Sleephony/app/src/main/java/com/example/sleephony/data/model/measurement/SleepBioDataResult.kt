package com.example.sleephony.data.model.measurement

import com.google.gson.annotations.SerializedName

data class SleepBioDataResult(
    @SerializedName("level") val level: String,
    @SerializedName("score") val score: Int,
    @SerializedName("measuredAt") val measuredAt: String
)