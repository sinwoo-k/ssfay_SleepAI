package com.example.sleephony.components.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 여기서 알람이 울릴 때 할 동작을 구현
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 500, 1000, 500)
        val action = intent.action
        Log.d("ssafy","$action")
        when (action) {
            "ALARM_START" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = VibrationEffect.createWaveform(pattern, 0)
                    vibrator.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(pattern, 0)
                }
            }
            "ALARM_CANCEL" -> {
                vibrator.cancel()
            }

        }
    }
}