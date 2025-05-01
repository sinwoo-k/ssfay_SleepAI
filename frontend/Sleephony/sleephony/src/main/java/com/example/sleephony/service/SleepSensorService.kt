package com.example.sleephony.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.*
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sleephony_wear.screens.isServiceRunning
import com.example.sleephony_wear.screens.sensorDataFlow
import com.example.sleephony.R

class SleepSensorService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private val TAG = "SensorService"

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 기존에 등록된 리스너 모두 해제
        sensorManager.unregisterListener(this)

        // 필요한 센서만 등록
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d(TAG, "가속도계 센서 등록됨")
        }

        val gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        if (gravity != null) {
            sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d(TAG, "중력 센서 등록됨")
        }

        val heartRate = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        if (heartRate != null) {
            sensorManager.registerListener(this, heartRate, SensorManager.SENSOR_DELAY_FASTEST)
            Log.d(TAG, "심박수 센서 등록됨")
        }

        startForegroundServiceWithNotification()
        isServiceRunning.value = true
        Log.d(TAG, "서비스 시작됨")
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "sensor_service_channel"
        val channelName = "Sensor Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("수면 센서 활성화됨")
            .setContentText("센서 데이터를 수집 중입니다...")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        startForeground(1, notification)
        Log.d(TAG, "포그라운드 서비스 시작됨")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        // 원하는 센서 타입인지 확인
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val values = event.values.joinToString(", ") { "%.2f".format(it) }
                updateSensorData("가속도계", values)
/*                Log.d(TAG, "가속도계: $values")*/
            }
            Sensor.TYPE_GRAVITY -> {
                val values = event.values.joinToString(", ") { "%.2f".format(it) }
                updateSensorData("중력 센서", values)
/*                Log.d(TAG, "중력 센서: $values")*/
            }
            Sensor.TYPE_HEART_RATE -> {
                val values = event.values.joinToString(", ") { "%.2f".format(it) }
                updateSensorData("심박수", values)
                Log.d(TAG, "심박수: $values")
            }
        }
    }

    private fun updateSensorData(sensorName: String, value: String) {
        // 현재 데이터 가져오기
        val currentData = sensorDataFlow.value.toMutableMap()
        // 새 데이터 추가
        currentData[sensorName] = value
        // Flow 업데이트
        sensorDataFlow.value = currentData
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        sensor?.let {
            Log.d(TAG, "센서 정확도 변경: ${sensorTypeToName(it.type)}, 정확도: $accuracy")
        }
    }

    private fun sensorTypeToName(type: Int): String {
        return when (type) {
            Sensor.TYPE_ACCELEROMETER -> "가속도계"
            Sensor.TYPE_GYROSCOPE -> "자이로스코프"
            Sensor.TYPE_HEART_RATE -> "심박수"
            else -> "알 수 없음 ($type)"
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand 호출됨")
        return START_STICKY // 서비스가 종료되어도 재시작
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        isServiceRunning.value = false
        // 센서 데이터 초기화
        sensorDataFlow.value = emptyMap()
        Log.d(TAG, "서비스 종료됨")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}