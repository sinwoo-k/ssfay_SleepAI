package com.example.sleephony.ui.screen.report.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephony.data.model.report.ReportResult
import com.example.sleephony.data.model.report.SleepDataResponse
import com.example.sleephony.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {

    private val _sleepReport = MutableStateFlow<ReportResult?>(null)
    val sleepReport: StateFlow<ReportResult?> = _sleepReport

    private val _sleepGraphData = MutableStateFlow<List<SleepDataResponse>>(emptyList())
    val sleepGraphData: StateFlow<List<SleepDataResponse>> = _sleepGraphData

    fun getSleepReport(date: String) {
        Log.d("ReportViewModel", "📅 getSleepReport() called with date: $date")

        viewModelScope.launch {
            val result = reportRepository.getReportDetail(date)
            result
                .onSuccess { report ->
                    Log.d("ReportViewModel", "✅ 수면 리포트 조회 성공: $report")
                    _sleepReport.value = report
                }
                .onFailure { error ->
                    Log.e("ReportViewModel", "🔥 수면 리포트 조회 실패: ${error.message}", error)
                }
        }
    }

    fun getSleepGraph(date: String) {
        Log.d("ReportViewModel", "📈 getSleepGraph() called with date: $date")

        viewModelScope.launch {
            val result = reportRepository.getSleepGraph(date)
            result
                .onSuccess { graph ->
                    Log.d("ReportViewModel", "✅ 수면 그래프 조회 성공: ${graph.size}개")
                    _sleepGraphData.value = graph
                }
                .onFailure { error ->
                    Log.e("ReportViewModel", "🔥 수면 그래프 조회 실패: ${error.message}", error)
                }
        }
    }
}
