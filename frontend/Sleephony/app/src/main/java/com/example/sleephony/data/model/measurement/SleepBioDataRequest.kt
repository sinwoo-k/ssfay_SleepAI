package com.example.sleephony.data.model.measurement

import com.google.gson.annotations.SerializedName

data class SleepBioDataRequest(
    @SerializedName("measuredAt") val measuredAt : String,
    @SerializedName("data") val data : List<BioDataDto>,
)