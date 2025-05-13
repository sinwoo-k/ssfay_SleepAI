package com.example.sleephony.data.model

data class StatisticSummaryResponse(
    val status : Int,
    val code : String,
    val message : String,
    val results:StatisticSummaryResults
)


data class StatisticSummaryResults(
    val summary:StatisticSummaryData,
    val myStatistics: List<StatisticMySummary>
)

data class StatisticSummaryData(
    val period : String,
    val averageSleepScore: Float,
    val averageSleepTimeMinutes: Float,
    val averageSleepLatencyMinutes: Float,
    val averageLightSleepMinutes: Float,
    val averageLightSleepPercentage : Int,
    val averageRemSleepMinutes:Float,
    val averageRemSleepPercentage : Int,
    val averageDeepSleepMinutes : Float,
    val averageDeepSleepPercentage : Int,
    val averageAwakeMinutes : Float,
    val averageAwakePercentage : Int,
    val averageSleepCycleCount : Int
)

data class StatisticMySummary(
    val sleepStatisticId: Int,
    val ageGroup : String,
    val gender : String,
    val bedtime : String,
    val wakeupTime: String,
    val sleepDurationMinutes : Int,
    val sleepLatencyMinutes : Int,
    val remSleepMinutes : Int,
    val remSleepRatio : Int,
    val lightSleepMinutes : Int,
    val lightSleepRatio :Int,
    val deepSleepMinutes : Int,
    val deepSleepRatio : Int
)

