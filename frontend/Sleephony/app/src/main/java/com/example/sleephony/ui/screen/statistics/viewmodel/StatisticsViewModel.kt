package com.example.sleephony.ui.screen.statistics.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephony.data.model.StatisticRequest
import com.example.sleephony.data.model.StatisticResponse
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
    private val _statistics = MutableStateFlow<StatisticResponse?>(null)
    val statistics: StateFlow<StatisticResponse?> = _statistics

    fun loadStatistics(startDate: String, endDate: String, periodType: String) {
        val request = StatisticRequest(startDate,endDate,periodType)

        viewModelScope.launch {
            val response = repositoryImpl.getStatistics(request)
            try {
                _statistics.value = response
                Log.d("ssafy","$response")
            } catch (e :Exception) {
                Log.d("ssafy","$e")
            }
        }
    }
}