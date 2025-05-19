package com.example.sleephony.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import com.example.sleephony.R
import com.example.sleephony.components.alarm.AlarmReceiver
import com.example.sleephony.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class SleepAlarmService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var vibrator: Vibrator
    private var waketime: String = ""

    companion object {
        private const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        startForeground()
    }


    private fun startForeground() {
        val channelId = "alarm_channel"
        val channelName = "Sleep Alarm"
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("수면 알람")
            .setContentText("알람이 설정되었습니다")
            .setSmallIcon(R.drawable.ion_alarm)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        waketime = intent?.getStringExtra("waketime") ?: ""
        startAlarmCheck(waketime)
        return START_NOT_STICKY
    }

    private fun startAlarmCheck(wakeTimeStr: String) {
        try {
            val formatter = DateTimeFormatter.ofPattern("a hh:mm")
            val wakeTime = LocalTime.parse(wakeTimeStr, formatter)

            val now = LocalDateTime.now()
            var alarmDateTime = LocalDateTime.of(now.toLocalDate(),wakeTime)

            if (alarmDateTime.isBefore(now)) {
                alarmDateTime = alarmDateTime.plusDays(1)
            }
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val alarmIntent = Intent(this, AlarmReceiver::class.java).apply {
                action = "ALARM_START"
            }
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val triggerAtMillis = alarmDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } catch (e: Exception) {
            Log.e("ssafy","알람 설정오류 $e")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator.cancel()
        serviceScope.cancel()

        val stopIntent = Intent(this, AlarmReceiver::class.java).apply {
            action = "ALARM_CANCEL"
        }
        this.sendBroadcast(stopIntent)

    }

    override fun onBind(p0: Intent?): IBinder? = null
}
