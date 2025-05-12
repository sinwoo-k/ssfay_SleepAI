package com.example.sleephony.data.model.theme

import com.google.gson.annotations.SerializedName

data class SoundDto(
    @SerializedName("soundId") val soundId: Int,
    @SerializedName("soundName") val soundName: String,
    @SerializedName("soundUrl") val soundUrl: String,
    @SerializedName("sleepStage") val sleepStage: String,
    @SerializedName("createdAt") val createdAt: String
)