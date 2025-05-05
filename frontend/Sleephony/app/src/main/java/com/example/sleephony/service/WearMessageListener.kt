package com.example.sleephony.service

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import org.json.JSONObject

class WearMessageListener :WearableListenerService() {
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
                Log.d("ssafy","bedtime $bedtime")
                Log.d("ssafy","wakeuptime $wakeUpTime")
            }
        }
    }
}