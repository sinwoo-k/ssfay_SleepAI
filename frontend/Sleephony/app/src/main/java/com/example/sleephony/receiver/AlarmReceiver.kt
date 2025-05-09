package com.example.sleephony.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.sleephony.service.AlarmForegroundService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        val context = ctx ?: return

        Intent(context, AlarmForegroundService::class.java).also { svc ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(svc)
            } else {
                context.startService(svc)
            }
        }

    }
}
