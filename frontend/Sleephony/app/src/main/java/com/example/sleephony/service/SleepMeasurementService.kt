package com.example.sleephony.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sleephony.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SleepMeasurementService : Service() {

    companion object {
            private const val CHANNEL_ID  = "Sleep_measurement"
            private const val FOREGROUND_ID = 2001
            private const val INTERVAL_MS = 1 * 60 * 1000L
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate() {
        super.onCreate()

        // 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                CHANNEL_ID,
                "수면 측정 중",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(chan)
        }

        // 포그라운드 알림
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("수면 측정 중")
            .setContentText("수면 데이터를 전송합니다.")
            .setSmallIcon(R.drawable.ic_sleep_phony)
            .build()
        startForeground(FOREGROUND_ID, notif)

        // PARTIAL_WAKE_LOCK -> CPU 유지
        val pm = getSystemService(PowerManager::class.java)
        wakeLock = pm.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "Sleephony:MeasurementLock"
        ).apply { acquire() }

        serviceScope.launch {
            while (isActive) {
                fetchAndUpload()
                delay(INTERVAL_MS)
            }
        }
    }

    private suspend fun fetchAndUpload() {
        Log.d("DBG", "측정 중입니다.")
        // 워치에서 데이터 가져오기

        // 서버에 데이터 보내기
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DBG", "수면 측정 중단")
        serviceScope.cancel()
        if(wakeLock.isHeld) wakeLock.release()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}