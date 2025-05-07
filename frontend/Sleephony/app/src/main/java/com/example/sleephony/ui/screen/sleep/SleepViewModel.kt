package com.example.sleephony.ui.screen.sleep


import androidx.lifecycle.ViewModel
import com.example.sleephony.domain.model.AlarmMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// 1) UI 상태 정의
sealed class SleepUiState {
    data object Setting : SleepUiState()                                  // 설정 화면
    data object Running : SleepUiState()                                  // 측정 중 화면
    data class Summary(val summary: SleepSummary) : SleepUiState()   // 요약 화면
}

// 2) 설정 화면에서 사용할 데이터 클래스
data class SleepSettingData(
    val hour: Int = 6,
    val minute: Int = 30,
    val isAm: Boolean = true,
    val mode: AlarmMode = AlarmMode.COMFORT
)

// 3) 요약에 표시할 샘플 데이터 클래스
data class SleepSummary(
    val durationMillis: Long,
    val deepSleepRatio: Float,
    val lightSleepRatio: Float,
    val wakeCount: Int
)

// 4) ViewModel
@HiltViewModel
class SleepViewModel @Inject constructor(

) : ViewModel() {

    // 현재 보여줄 화면 상태 (Setting, Running, Summary)
    private val _uiState = MutableStateFlow<SleepUiState>(SleepUiState.Setting)
    val uiState: StateFlow<SleepUiState> = _uiState.asStateFlow()

    // 설정 화면에서 조작된 시간·모드 값
    private val _settingData = MutableStateFlow(SleepSettingData())
    val settingData: StateFlow<SleepSettingData> = _settingData.asStateFlow()

    /** 시간 변경 이벤트 */
    fun onTimeChanged(hour: Int, minute: Int, isAm: Boolean) {
        _settingData.update { it.copy(hour = hour, minute = minute, isAm = isAm) }
    }

    /** 모드 선택 이벤트 */
    fun onModeSelected(mode: AlarmMode) {
        _settingData.update { it.copy(mode = mode) }
    }

    /** 측정 시작 버튼 클릭 */
    fun onStartClicked() {
        _uiState.value = SleepUiState.Running
    }

    /** 측정 중단(혹은 알람 발생) 이벤트 */
    fun onStopClicked() {
        // 임시 더미 요약 생성 예시
        val dummySummary = SleepSummary(
            durationMillis = 7 * 60 * 60 * 1000L,
            deepSleepRatio = 0.25f,
            lightSleepRatio = 0.60f,
            wakeCount = 3
        )
        _uiState.value = SleepUiState.Summary(dummySummary)
    }

    /** 다시 설정 화면으로 돌아가기 */
    fun onRestart() {
        _settingData.value = SleepSettingData()
        _uiState.value = SleepUiState.Setting
    }
}
