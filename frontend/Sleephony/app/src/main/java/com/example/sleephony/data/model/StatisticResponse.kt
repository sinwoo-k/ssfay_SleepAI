package com.example.sleephony.data.model

data class StatisticResponse(
    val status :Int,
    val code : String,
    val message : String,
    val results :StatisticResults
)

data class StatisticResults(
    val sleepTime: List<StatisticData>,
    val sleepLatency : List<StatisticData>,
    val lightSleep : List<StatisticData>,
    val remSleep : List<StatisticData>,
    val deepSleep : List<StatisticData>,
    val awakeTime : List<StatisticData>,
)

data class StatisticData (
    val label : String,
    val value : Int
)