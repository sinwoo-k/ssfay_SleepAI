package com.example.sleephony.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.NotificationCompat
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


class SleepAlarmService: android.app.Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var vibrator: Vibrator
    private var waketime:String = ""


    companion object {
        private const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        startForeground()
    }

    private fun startForeground() {
        val channedId = "alarm_channel"
        val channelName = "Sleep Alarm"
        val channel = NotificationChannel(
            channedId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channedId)
            .setContentTitle("수면 알람")
            .setContentText("알람이 설정되었습니다")
            .setSmallIcon(R.drawable.ion_alarm)
            .build()

        startForeground(NOTIFICATION_ID,notification)
    }

    private fun startAlarmCheck() {
        serviceScope.launch {
            while (true) {
                val now = LocalTime.now()
                val formatter = DateTimeFormatter.ofPattern("a hh:mm")
                val currentTime = now.format(formatter)

                if (currentTime == waketime) {
                    triggerAlarm()
                }
                delay(5000)
            }
        }
    }

    private fun triggerAlarm() {
        val effect = VibrationEffect.createOneShot(5000,VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(effect)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags= Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("alarm_trigger",true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }


}