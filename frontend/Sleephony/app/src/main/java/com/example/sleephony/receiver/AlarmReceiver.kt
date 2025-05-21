package com.example.sleephony.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.sleephony.service.AlarmForegroundService
import com.example.sleephony.ui.screen.sleep.SleepViewModel

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        val context = ctx ?: return

        // 포그라운드 서비스 시작
        Intent(context, AlarmForegroundService::class.java).also { svc ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(svc)
            } else {
                context.startService(svc)
            }
        }
    }
}
