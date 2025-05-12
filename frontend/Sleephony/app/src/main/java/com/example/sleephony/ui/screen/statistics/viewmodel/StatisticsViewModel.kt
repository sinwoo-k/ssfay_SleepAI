package com.example.sleephony.ui.screen.statistics.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephony.data.model.StatisticMySummary
import com.example.sleephony.data.model.StatisticRequest
import com.example.sleephony.data.model.StatisticResponse
import com.example.sleephony.data.model.StatisticResults
import com.example.sleephony.data.model.StatisticSummaryData
import com.example.sleephony.data.model.StatisticSummaryRequest
import com.example.sleephony.data.model.StatisticSummaryResults
import com.example.sleephony.data.repository.StatisticsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repositoryImpl: StatisticsRepositoryImpl
) : ViewModel() {
    private val _statistics = MutableStateFlow<StatisticResults?>(null)
    val statistics: StateFlow<StatisticResults?> = _statistics

    private val _statisticSummary = MutableStateFlow<StatisticSummaryData?>(null)
    val statisticSummary: StateFlow<StatisticSummaryData?> = _statisticSummary

    private val _statisticMySummary = MutableStateFlow<List<StatisticMySummary?>>(emptyList())
    val statisticMySummary: StateFlow<List<StatisticMySummary?>> = _statisticMySummary

    fun loadStatistics(startDate: String, endDate: String, periodType: String) {
        val request = StatisticRequest(startDate,endDate,periodType)

        viewModelScope.launch {
            try {
                val response = repositoryImpl.getStatistics(request)
                if (response.status == 200) {
                    _statistics.value = response.results

                }
                Log.d("ssafy","$response")
            } catch (e :Exception) {
                Log.e("ssafy","에러 $e")
            }
        }
    }

    fun loadStatisticSummary(startDate: String, endDate: String, periodType: String) {
        val request = StatisticSummaryRequest(startDate,endDate,periodType)

        viewModelScope.launch {
            try {
                val response = repositoryImpl.getStatisticsSummary(request)
                if (response.status == 200) {
                    _statisticSummary.value = response.results.summary
                    _statisticMySummary.value = response.results.myStatistics
                }
            } catch (e: Exception) {
                Log.d("ssafy","$e")
            }
        }
    }
}