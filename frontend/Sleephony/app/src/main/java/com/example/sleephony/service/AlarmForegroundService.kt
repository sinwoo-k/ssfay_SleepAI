package com.example.sleephony.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.example.sleephony.R

class AlarmForegroundService : Service() {

    companion object {
        const val FOREGROUND_ID = 1002
        // 알림 채널 ID
        const val CHANNEL_ID = "sleep_alarm_service"
    }

    private lateinit var vibrator: Vibrator

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // (1) NotificationChannel 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                CHANNEL_ID,
                "알람 서비스 채널",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "알람 진동 및 사운드 재생용"
                setBypassDnd(true)
            }
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(chan)
        }

        // (2) 포그라운드 알림
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("⏰ 알람 중")
            .setContentText("알람을 끄려면 화면에서 ‘끄기’ 버튼을 눌러주세요.")
            .setSmallIcon(R.drawable.ic_sleep_phony)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()
        startForeground(FOREGROUND_ID, notif)

        // (3) 진동 시작
        startVibration()
    }

    private fun startVibration() {
        // 진동 패턴: [대기, 진동, 대기, 진동, ...], -1은 반복 안 함, 0 이상은 인덱스 순환 반복
        val pattern = longArrayOf(0, 1000, 500)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 서비스가 끝날 때 진동도 멈추기
        vibrator.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
