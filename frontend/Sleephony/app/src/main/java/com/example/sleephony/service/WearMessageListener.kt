package com.example.sleephony.service

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class WearMessageListener :WearableListenerService() {
    override fun onCreate() {
        super.onCreate()
        Log.d("ssafy", "WearMessageListener 서비스 생성됨")
    }


    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d("ssafy","receive")
        Log.d("ssafy","path = ${messageEvent.path}")

        if (messageEvent.path == "/alarm") {
            val message = String(messageEvent.data)
            Log.d("ssafy","message $message")
        }
    }
}