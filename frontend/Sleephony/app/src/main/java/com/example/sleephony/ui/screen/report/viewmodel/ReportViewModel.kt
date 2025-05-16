package com.example.sleephony.ui.screen.report.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephony.data.model.report.AiReportResponse
import com.example.sleephony.data.model.report.ReportResponse
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

    private val _sleepReport = MutableStateFlow<ReportResponse?>(null)
    val sleepReport: StateFlow<ReportResponse?> = _sleepReport

    private val _sleepGraphData = MutableStateFlow<List<SleepDataResponse>>(emptyList())
    val sleepGraphData: StateFlow<List<SleepDataResponse>> = _sleepGraphData

    private val _aiReport = MutableStateFlow<AiReportResponse?>(null)
    val aiReport: StateFlow<AiReportResponse?> = _aiReport

    private val _aiReportText = MutableStateFlow("")
    val aiReportText: StateFlow<String> = _aiReportText

    fun getSleepReport(date: String) {
        Log.d("ReportViewModel", "📅 getSleepReport() called with date: $date")
        viewModelScope.launch {
            reportRepository.getReportDetail(date)
                .onSuccess {
                    Log.d("ReportViewModel", "✅ 수면 리포트 조회 성공")
                    _sleepReport.value = it
                }
                .onFailure {
                    Log.e("ReportViewModel", "🔥 수면 리포트 조회 실패: ${it.message}", it)
                }
        }
    }

    fun getSleepGraph(date: String) {
        Log.d("ReportViewModel", "📈 getSleepGraph() called with date: $date")
        viewModelScope.launch {
            reportRepository.getSleepGraph(date)
                .onSuccess {
                    Log.d("ReportViewModel", "✅ 수면 그래프 조회 성공: ${it.size}개")
                    _sleepGraphData.value = it
                }
                .onFailure {
                    Log.e("ReportViewModel", "🔥 수면 그래프 조회 실패: ${it.message}", it)
                }
        }
    }

    fun getReportDetailed(date: String) {
        Log.d("ReportViewModel", "🧠 getReportDetailed() called with date: $date")
        viewModelScope.launch {
            reportRepository.getReportDetailed(date)
                .onSuccess {
                    Log.d("ReportViewModel", "✅ AI 분석 리포트 조회 성공")
                    _aiReport.value = it
                }
                .onFailure {
                    Log.e("ReportViewModel", "🔥 AI 분석 리포트 조회 실패: ${it.message}", it)
                }
        }
    }

    fun getAiReport(date: String) {
        Log.d("ReportViewModel", "📜 getAiReport() called with date: $date")
        viewModelScope.launch {
            reportRepository.getAiReport(date)
                .onSuccess {
                    Log.d("ReportViewModel", "✅ AI 분석 텍스트 조회 성공")
                    _aiReportText.value = it
                }
                .onFailure {
                    Log.e("ReportViewModel", "🔥 AI 분석 텍스트 조회 실패: ${it.message}", it)
                }
        }
    }
}
