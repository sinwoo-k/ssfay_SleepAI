package com.example.sleephony.data.model

import com.google.gson.annotations.SerializedName

data class KakaoLoginRequest(
    @SerializedName("access_token")
    val accessToken: String
)