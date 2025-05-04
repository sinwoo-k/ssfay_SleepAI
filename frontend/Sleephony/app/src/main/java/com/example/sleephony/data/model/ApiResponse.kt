package com.example.sleephony.data.model

class ApiResponse<T> (
    val status: Int,
    val code: String,
    val message: String,
    val results: T?
)