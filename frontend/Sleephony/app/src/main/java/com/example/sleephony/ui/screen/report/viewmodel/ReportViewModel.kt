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

    private val _isLoadingAiReport = MutableStateFlow(false)
    val isLoadingAiReport: StateFlow<Boolean> = _isLoadingAiReport

    private val _reportDateList = MutableStateFlow<List<String>>(emptyList())
    val reportDateList: StateFlow<List<String>> = _reportDateList

    fun getSleepReport(date: String) {
        Log.d("ReportViewModel", "📅 getSleepReport() called with date: $date")
        viewModelScope.launch {
            try {
                reportRepository.getReportDetail(date).onSuccess {
                    Log.d("ReportViewModel", "✅ 수면 리포트 조회 성공")
                    _sleepReport.value = it
                }.onFailure {
                    Log.e("ReportViewModel", "🔥 수면 리포트 조회 실패: ${it.message}", it)
                    _sleepReport.value = null // 실패시 null 처리
                }
            } catch (e: Exception) {
                Log.e("ReportViewModel", "🔥 수면 리포트 조회 예외: ${e.message}", e)
                _sleepReport.value = null // 예외 발생 시 null 처리
            }
        }
    }

    fun getSleepGraph(date: String) {
        Log.d("ReportViewModel", "📈 getSleepGraph() called with date: $date")
        viewModelScope.launch {
            try {
                reportRepository.getSleepGraph(date)
                    .onSuccess {
                        Log.d("ReportViewModel", "✅ 수면 그래프 조회 성공: ${it.size}개")
                        _sleepGraphData.value = it
                    }
                    .onFailure {
                        Log.e("ReportViewModel", "🔥 수면 그래프 조회 실패: ${it.message}", it)
                        _sleepGraphData.value = emptyList() // 실패시 빈 리스트 처리
                    }
            } catch (e: Exception) {
                Log.e("ReportViewModel", "🔥 수면 그래프 조회 예외: ${e.message}", e)
                _sleepGraphData.value = emptyList() // 예외 발생 시 빈 리스트 처리
            }
        }
    }

    fun getReportDetailed(date: String) {
        Log.d("ReportViewModel", "🧠 getReportDetailed() called with date: $date")
        viewModelScope.launch {
            try {
                reportRepository.getReportDetailed(date)
                    .onSuccess {
                        Log.d("ReportViewModel", "✅ AI 분석 리포트 조회 성공")
                        _aiReport.value = it
                    }
                    .onFailure {
                        Log.e("ReportViewModel", "🔥 AI 분석 리포트 조회 실패: ${it.message}", it)
                        _aiReport.value = null // 실패시 null 처리
                    }
            } catch (e: Exception) {
                Log.e("ReportViewModel", "🔥 AI 분석 리포트 조회 예외: ${e.message}", e)
                _aiReport.value = null // 예외 발생 시 null 처리
            }
        }
    }

    fun getAiReport(date: String) {
        Log.d("ReportViewModel", "📜 getAiReport() called with date: $date")
        viewModelScope.launch {
            _isLoadingAiReport.value = true // 로딩 시작
            try {
                reportRepository.getAiReport(date)
                    .onSuccess {
                        Log.d("ReportViewModel", "✅ AI 분석 텍스트 조회 성공")
                        _aiReportText.value = it
                        _isLoadingAiReport.value = false // 성공 시 로딩 종료
                    }
                    .onFailure {
                        Log.e("ReportViewModel", "🔥 AI 분석 텍스트 조회 실패: ${it.message}", it)
                        _aiReportText.value = "" // 실패시 빈 문자열 처리
                        _isLoadingAiReport.value = false // 실패 시 로딩 종료
                    }
            } catch (e: Exception) {
                Log.e("ReportViewModel", "🔥 AI 분석 텍스트 조회 예외: ${e.message}", e)
                _aiReportText.value = "" // 예외 발생 시 빈 문자열 처리
                _isLoadingAiReport.value = false // 예외 시 로딩 종료
            }
        }
    }

    fun getReportDates(month: String) {
        Log.d("ReportViewModel", "📆 getReportDates() called with month: $month")
        viewModelScope.launch {
            try {
                reportRepository.getReportDates(month)
                    .onSuccess {
                        Log.d("ReportViewModel", "✅ 기록된 수면 날짜 조회 성공: ${it.size}개")
                        _reportDateList.value = it
                    }
                    .onFailure {
                        Log.e("ReportViewModel", "🔥 기록된 수면 날짜 조회 실패: ${it.message}", it)
                        _reportDateList.value = emptyList() // 실패시 빈 리스트 처리
                    }
            } catch (e: Exception) {
                Log.e("ReportViewModel", "🔥 기록된 수면 날짜 조회 예외: ${e.message}", e)
                _reportDateList.value = emptyList() // 예외 발생 시 빈 리스트 처리
            }
        }
    }
}