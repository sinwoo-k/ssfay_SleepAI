package com.example.sleephony.service

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import org.json.JSONObject

class AppMessageListener: WearableListenerService() {

    override fun onCreate() {
        super.onCreate()
        Log.d("ssafy", "WearMessageListener 서비스 생성됨")
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/alarm") {
            val message = String(messageEvent.data)
            val jsonData = JSONObject(message)
            val mode = jsonData.getString("mode")
            Log.d("ssafy","$jsonData")
            if (mode == "profile") {
                val profileData = getSharedPreferences("user_profile", MODE_PRIVATE)
                with(profileData.edit()) {
                    putString("email",jsonData.getString("email"))
                    putString("nickname",jsonData.getString("nickname"))
                    putString("height",jsonData.getString("height"))
                    putString("weight",jsonData.getString("weight"))
                    putString("birthDate",jsonData.getString("birthDate"))
                    putString("gender",jsonData.getString("gender"))
                    apply()
                }
            } else if (mode == "history") {
                val historyData = getSharedPreferences("user_history", MODE_PRIVATE)
                with(historyData.edit()) {
                    putString("${jsonData.getString("day")}-value",jsonData.getString("value"))
                    apply()
                }
            }
        }
    }
}