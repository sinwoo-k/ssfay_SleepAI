package com.example.sleephony.data.model

import com.google.gson.annotations.SerializedName

data class UserProfileRequest(
    val nickname: String,
    val height: Int,
    val weight: Int,
    @SerializedName("birthDate")
    val birthDate: String,   // "YYYY-MM-DD" 형식
    val gender: String        // "M" 또는 "F"
)