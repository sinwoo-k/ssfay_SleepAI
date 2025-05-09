package com.example.sleephony.ui.screen.sleep


import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.sleephony.domain.model.AlarmMode
import com.example.sleephony.receiver.AlarmReceiver
import com.example.sleephony.service.SleepMeasurementService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject


sealed class SleepUiState {
    data object Setting : SleepUiState()                                  // 설정 화면
    data object Running : SleepUiState()                                  // 측정 중 화면
}

// 설정 화면에서 사용할 데이터 클래스
data class SleepSettingData(
    val hour: Int = 6,
    val minute: Int = 30,
    val isAm: Boolean = true,
    val mode: AlarmMode = AlarmMode.COMFORT
)


@HiltViewModel
class SleepViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    companion object {
        const val ACTION_STOP_MEASUREMENT = "com.example.sleephony.ACTION_STOP_MEASUREMENT"
    }

    // 현재 보여줄 화면 상태 (Setting, Running, Summary)
    private val _uiState = MutableStateFlow<SleepUiState>(SleepUiState.Setting)
    val uiState: StateFlow<SleepUiState> = _uiState.asStateFlow()

    // 설정 화면에서 조작된 시간·모드 값
    private val _settingData = MutableStateFlow(SleepSettingData())
    val settingData: StateFlow<SleepSettingData> = _settingData.asStateFlow()

    private val stopReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_STOP_MEASUREMENT) {
                Log.d("DBG", "측정 중단 요청 받기")
                onStopClicked()
            }
        }
    }

    init {
        val filter = IntentFilter(ACTION_STOP_MEASUREMENT)
        ContextCompat.registerReceiver(
            appContext,
            stopReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onCleared() {
        super.onCleared()
        appContext.unregisterReceiver(stopReceiver)
    }

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
        // 알람 권한 설정
        ensureExactAlarmPermission(appContext)

        // 알람 예약
        val sd = _settingData.value
        val hour24 = to24Hour(sd.hour, sd.isAm)
        scheduleWakeUp(appContext, hour24, sd.minute)

        // 측정 시작
        Intent(appContext, SleepMeasurementService::class.java).also {
            ContextCompat.startForegroundService(appContext, it)
        }

        _uiState.value = SleepUiState.Running
    }

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    private fun scheduleWakeUp(context: Context, hour: Int, minute: Int){
        // 1) 시간 계산은 그대로
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DATE, 1)
        }

        // 2) 브로드캐스트용 PendingIntent (AlarmReceiver 호출)
        val broadcastIntent = Intent(context, AlarmReceiver::class.java)
        val broadcastPi = PendingIntent.getBroadcast(
            context, 0, broadcastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 3) 액티비티용 PendingIntent (풀스크린 인텐트용)
        val activityIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val activityPi = PendingIntent.getActivity(
            context, 0, activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 4) AlarmClockInfo: 두 번째 파라미터로 Activity용 PendingIntent 전달
        val info = AlarmManager.AlarmClockInfo(calendar.timeInMillis, activityPi)

        // 5) setAlarmClock 에는 “실제로 시간이 되면 호출할” Broadcast용 PendingIntent 사용
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setAlarmClock(info, broadcastPi)
    }

    /** 측정 중단(혹은 알람 발생) 이벤트 */
    fun onStopClicked() {
        // 알람 취소
        cancelScheduledWakeUp(appContext)

        // 측정 중단
        appContext.stopService(Intent(appContext, SleepMeasurementService::class.java))


        _settingData.value = SleepSettingData()
        _uiState.value = SleepUiState.Setting
    }

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    private fun cancelScheduledWakeUp(context: Context) {
        val intent = Intent(context, AlarmReceiver::class.java)
        // 예약 시와 동일한 PendingIntent 플래그 사용
        val pi = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pi)     // AlarmManager 에서 알람 취소
        pi.cancel()       // PendingIntent 자체도 해제
    }

    private fun to24Hour(hour12: Int, isAm: Boolean): Int {
        return when {
            isAm && hour12 == 12 -> 0        // 12 AM → 0시
            !isAm && hour12 < 12 -> hour12 + 12  // PM이고 1~11시 → 13~23시
            else -> hour12                  // 나머지 (1~11 AM, 12 PM)
        }
    }

    // 알람 권한 확인하기
    private fun ensureExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= 33) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!am.canScheduleExactAlarms()) {
                context.startActivity(
                    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
        }
    }

}
