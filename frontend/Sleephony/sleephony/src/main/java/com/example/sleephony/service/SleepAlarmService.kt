package com.example.sleephony.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
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
import com.example.sleephony.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
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
        startAlarmCheck()
        return START_STICKY
    }

    private fun startAlarmCheck() {
        serviceScope.launch {
            while (true) {
                val now = LocalTime.now()
                val formatter = DateTimeFormatter.ofPattern("a hh:mm")
                val currentTime = now.format(formatter)

                if (currentTime == waketime) {
                    triggerAlarm()
                    break
                }

                delay(5000)
            }
        }
    }

    private fun triggerAlarm() {
        val pattern = longArrayOf(0, 500, 1000, 500)
        val effect = VibrationEffect.createWaveform(pattern,0)
        vibrator.vibrate(effect)

    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator.cancel()
        serviceScope.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? = null
}
