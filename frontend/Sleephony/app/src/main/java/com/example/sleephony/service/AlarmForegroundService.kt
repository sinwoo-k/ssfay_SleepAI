package com.example.sleephony.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.sleephony.R
import com.example.sleephony.ui.screen.sleep.AlarmActivity

class AlarmForegroundService : Service() {

    companion object {
        const val FOREGROUND_ID = 1002
        // 알림 채널 ID
        const val CHANNEL_ID = "sleep_alarm_service"

        // 알람 최대 시간 설정
        private const val MAX_RING_DURATION = 1 * 60 * 1000L
    }

    private lateinit var vibrator: Vibrator
    private var mediaPlayer: MediaPlayer? = null

    private val autoStopHandler = Handler(Looper.getMainLooper())
    private val autoStopRunnable = Runnable {
        stopSelf()
    }

    override fun onCreate() {
        super.onCreate()

        autoStopHandler.postDelayed(autoStopRunnable, MAX_RING_DURATION)

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 이상: VibratorManager 통해 기본 진동기 얻기
            ContextCompat.getSystemService(this, VibratorManager::class.java)
                ?.defaultVibrator
                ?: throw IllegalStateException("VibratorManager가 없습니다")
        } else {
            // Android 11 이하: 기존 방식 유지
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        mediaPlayer = MediaPlayer().apply {
//            setDataSource(this@AlarmForegroundService, alarmUri)
//            isLooping = true
//            setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .build()
//            )
//            prepare()
//            start()
//        }

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

        val fullScreenIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, AlarmActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 포그라운드 알림
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("⏰ 알람 중")
            .setContentText("알람을 확인해주세요.")
            .setSmallIcon(R.drawable.ic_sleep_phony)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenIntent, true)
            .setContentIntent(fullScreenIntent)
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

        autoStopHandler.removeCallbacks(autoStopRunnable)

        // 서비스가 끝날 때 진동도 멈추기
        vibrator.cancel()

//        mediaPlayer?.let {
//            it.stop()
//            it.release()
//        }
//        mediaPlayer = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
