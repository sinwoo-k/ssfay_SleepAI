package com.example.sleephony.service

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sleephony.MainActivity
import com.example.sleephony.ui.screen.statistics.components.parsingTime
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
            try {
                val message = String(messageEvent.data)
                val jsonData = JSONObject(message)
                val mode = jsonData.getString("mode")
                if (mode == "alarm") {
                    val wakeUpTime = jsonData.getString("wakeUpTime")
                    val alarmType = jsonData.getString("alarmType")
                    val (hour, minute, isAm) = parseTime(wakeUpTime)

                    val intent = Intent(this, MainActivity::class.java).apply {
                        action = "alarmOpen"
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        putExtra("hour",hour)
                        putExtra("minute",minute)
                        putExtra("isAm",isAm)
                        putExtra("alarmType",alarmType)
                    }
                    startActivity(intent)
                }
                if (mode == "alarmCancel") {
                    val intent = Intent(this,MainActivity::class.java).apply {
                        action = "alarmCancel"
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                    startActivity(intent)
                }
                if (mode == "senser") {
                    Log.d("ssafy", "$jsonData")

                    val sensorData = jsonData.toString()

                    val broadcastIntent = Intent("com.example.sleephony.SENSOR_DATA").apply {
                        putExtra("sensorData", sensorData)
                    }
                    sendBroadcast(broadcastIntent)
                }
            } catch (e : Exception) {
                Log.e("ssafy", "$e")
            }
        }
    }
    private fun parseTime(time: String) : Triple<Int,Int, Boolean> {
        val (isAmStr, hourStr, minStr) = time.split(" ").map { it }
        val hour = if (hourStr.toInt() == 0) 12 else hourStr.toInt()
        val min = minStr.toInt()
        val isAm = if (isAmStr == "오전") true else false
        return Triple(hour, min, isAm)
    }
}
