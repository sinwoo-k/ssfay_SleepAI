package com.example.sleephony.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.*
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sleephony.R
import com.example.sleephony.screens.sensorDataFlow
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

class SleepSensorService : Service(), SensorEventListener {

    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private val sensorDataFlow = MutableStateFlow<Map<String,String>>(emptyMap())
    private lateinit var sensorManager: SensorManager

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }

        val gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        if (gravity != null) {
            sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL)
        }

        val heartRate = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        if (heartRate != null) {
            sensorManager.registerListener(this, heartRate, SensorManager.SENSOR_DELAY_FASTEST)
        }

        startForegroundServiceWithNotification()
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "sensor_service_channel"
        val channelName = "Sensor Service"
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("수면 센서 활성화됨")
            .setContentText("센서 데이터를 수집 중입니다...")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        startForeground(1, notification)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val values = event.values.joinToString(", ") { "%.2f".format(it) }
                val currentData = sensorDataFlow.value.toMutableMap()
                currentData["accelerometer"] = values
                sensorDataFlow.value = currentData
            }
            Sensor.TYPE_GRAVITY -> {
                val values = event.values.joinToString(", ") { "%.2f".format(it) }
                val currentData = sensorDataFlow.value.toMutableMap()
                currentData["gravity"] = values
                sensorDataFlow.value = currentData
            }
            Sensor.TYPE_HEART_RATE -> {
                val values = event.values.joinToString(", ") { "%.2f".format(it) }
                val currentData = sensorDataFlow.value.toMutableMap()
                currentData["heartRate"] = values
                sensorDataFlow.value = currentData
                serviceScope.launch {
                    sendMessage()
                }
            }
        }
    }

    private suspend fun sendMessage(){
        try {
            val nodeClient = Wearable.getNodeClient(this)
            val messageClient = Wearable.getMessageClient(this)

            val accelerometer = sensorDataFlow.value.getValue("accelerometer")
            val gravity = sensorDataFlow.value.getValue("gravity")
            val hearRate = sensorDataFlow.value.getValue("heartRate")
            Log.d("ssafy","accelerometer ${accelerometer} gravity ${gravity}  hearRate ${hearRate}")

            val jsonData = JSONObject().apply {
                put("mode","senser")
                put("accelerometer","${accelerometer}")
                put("gravity","${gravity}")
                put("hearRate","${hearRate}")
            }
            val jsonString = jsonData.toString()

            val nodes = nodeClient.connectedNodes.await()
            for (node in nodes) {
                messageClient.sendMessage(
                    node.id,
                    "/alarm",
                    jsonString.toByteArray()
                ).await()
            }
        } catch (e:Exception) {
            Log.d("ssafy","$e")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        sensorDataFlow.value = emptyMap()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}