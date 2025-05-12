package com.example.sleephony.data.model.user

data class UserProfileResult(
    val email: String,
    val nickname: String,
    val height: Int,
    val weight: Int,
    val birthDate: String,
    val gender: String
)