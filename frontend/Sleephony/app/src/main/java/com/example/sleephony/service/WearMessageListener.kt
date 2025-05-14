package com.example.sleephony.service

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sleephony.MainActivity
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import org.json.JSONObject

class WearMessageListener :WearableListenerService(
) {

    override fun onCreate() {
        super.onCreate()
        Log.d("ssafy", "WearMessageListener 서비스 생성됨")
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/alarm") {
            val message = String(messageEvent.data)
            val jsonData = JSONObject(message)
            val mode = jsonData.getString("mode")
            if (mode == "alarm") {
                val bedtime = jsonData.getString("bedTime")
                val wakeUpTime = jsonData.getString("wakeUpTime")
                val alarmType = jsonData.getString("alarmType")
                val intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra("bedtime",bedtime)
                    putExtra("wakeUpTime",wakeUpTime)
                    putExtra("alarmType",alarmType)
                }
                startActivity(intent)
            } else if (mode == "senser") {
                Log.d("ssafy","$jsonData")
            }
        }
    }
}
