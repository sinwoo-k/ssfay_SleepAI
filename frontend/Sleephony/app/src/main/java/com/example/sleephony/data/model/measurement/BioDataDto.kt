package com.example.sleephony.data.model.measurement

import com.google.gson.annotations.SerializedName

data class BioDataDto(
    @SerializedName("time") val time: String,
    @SerializedName("heartRate") val heartRate: Int,
    @SerializedName("temperature") val temperature: Float,
    @SerializedName("gyroX") val gyroX: Float,
    @SerializedName("gyroY") val gyroY: Float,
    @SerializedName("gyroZ") val gyroZ: Float
)