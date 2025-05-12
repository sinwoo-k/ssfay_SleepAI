package com.example.sleephony.data.model.theme

import com.google.gson.annotations.SerializedName

data class ThemeResult (
    @SerializedName("themeId") val themeId: Int,
    @SerializedName("themeName") val themeName: String,
    @SerializedName("themeDescription") val themeDescription: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("sounds") val sounds: List<SoundDto>
)