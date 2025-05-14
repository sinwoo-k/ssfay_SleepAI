package com.example.sleephony.data.model.report

data class SleepDataResponse(
    val measuredAt: String,  // "2025-05-12T23:00:00"
    val level: String,       // "REM", "NREM2", ...
    val score: Int
)