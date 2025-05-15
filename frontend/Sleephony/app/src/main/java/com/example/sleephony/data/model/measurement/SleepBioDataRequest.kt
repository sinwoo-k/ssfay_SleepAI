package com.example.sleephony.data.model.measurement

import com.google.gson.annotations.SerializedName

data class SleepBioDataRequest(
    @SerializedName("measured_at") val measuredAt : String,
    @SerializedName("acc_x") val accX : List<Double>,
    @SerializedName("acc_y") val accY : List<Double>,
    @SerializedName("acc_z") val accZ : List<Double>,
    @SerializedName("hr") val hr : List<Double>,
    @SerializedName("temp") val temp : List<Double>
)