package com.example.sleephony.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.sleephony.R
import com.example.sleephony.service.AlarmForegroundService
import com.example.sleephony.ui.screen.sleep.AlarmActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        val context = ctx ?: return
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "sleep_alarm_channel"


        // ① 채널 생성 (한 번만)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "알람 채널",
                NotificationManager.IMPORTANCE_HIGH // IMPORTANCE_HIGH 필수
            ).apply {
                description = "잠금화면 풀스크린 알람용"
                setBypassDnd(true)  // DND 무시
            }
            nm.createNotificationChannel(channel)
        }

        // ② 풀스크린 인텐트 준비
        val fullScreenIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, AlarmActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // ③ 알림 빌드
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_sleep_phony)              // 알맞은 아이콘
            .setContentTitle("⏰ 기상 알람")
            .setContentText("설정된 시간이 되었습니다.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)   // HIGH 필수
            .setCategory(NotificationCompat.CATEGORY_ALARM)  // CATEGORY_ALARM 필수
            .setFullScreenIntent(fullScreenIntent, true)
            .setAutoCancel(true)
            .build()

        nm.notify(1001, notification)

        Intent(context, AlarmForegroundService::class.java).also { svc ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(svc)
            } else {
                context.startService(svc)
            }
        }

        // 기존 wake lock (필요시 유지)
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        pm.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE,
            "Sleephony:AlarmLock"
        ).apply { acquire(5_000L) }
    }
}
