package com.example.sleephony.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.sleephony.R
import com.example.sleephony.data.datasource.local.ThemeLocalDataSource
import com.example.sleephony.data.model.measurement.SleepBioDataRequest
import com.example.sleephony.data.model.theme.SoundDto
import com.example.sleephony.domain.model.AlarmMode
import com.example.sleephony.domain.repository.MeasurementRepository
import com.example.sleephony.domain.repository.ThemeRepository
import com.example.sleephony.receiver.AlarmReceiver
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class SleepMeasurementService : Service() {

    private var mode: AlarmMode = AlarmMode.EXACT
    private val targetStage = "NREM1"
    private var startTimestamp: Long = Long.MIN_VALUE
    private var endTimestamp: Long = Long.MAX_VALUE

    companion object {
            private const val CHANNEL_ID  = "Sleep_measurement"
            private const val FOREGROUND_ID = 2001
            private const val INTERVAL_MS = 1 * 30 * 1000L  // 30초 요청
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var wakeLock: PowerManager.WakeLock

    @Inject lateinit var measurementRepository: MeasurementRepository
    @Inject lateinit var themeRepository: ThemeRepository
    @Inject lateinit var themeLocalDataSource: ThemeLocalDataSource

    private lateinit var mediaPlayer: MediaPlayer

    private val soundMap = mutableMapOf<String, String>()

    private suspend fun initializeSoundMap() {
        val themeId = themeLocalDataSource.themeIdFlow.first()
        themeRepository.getTheme(themeId)
            .onSuccess { theme ->
                initSoundMapFromTheme(themeId, theme.sounds)
            }
            .onFailure {
                Log.e("ERR", "테마 정보 로딩 실패", it)
            }
    }

    // 사운드 경로 얻는 함수
    private fun getSoundFilePath(themeId: Int, soundId: Int): String {
        val soundDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        return "${soundDir?.absolutePath}/theme_${themeId}/sound_${soundId}.mp3"
    }

    // 서버에서 받은 정보로 soundMap 초기화
    private fun initSoundMapFromTheme(themeId: Int, sounds: List<SoundDto>) {
        soundMap.clear()
        sounds.forEach { sound ->
            val localPath = getSoundFilePath(themeId, sound.soundId)
            soundMap[sound.sleepStage] = localPath
        }
    }
    // 센서 데이터 받아오기
    private val sensorDataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val data = intent?.getStringExtra("sensorData")
            Log.d("DBG", "Wear OS 센서 데이터: $data")

            // 👉 여기서 원하는 처리 (예: 서버로 전송, DB 저장 등)
        }
    }

    override fun onCreate() {
        super.onCreate()
        // 센서 데이터 응답
        ContextCompat.registerReceiver(
            this,
            sensorDataReceiver,
            IntentFilter("com.example.sleephony.SENSOR_DATA"),
            ContextCompat.RECEIVER_EXPORTED
        )

        // 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                CHANNEL_ID,
                "수면 측정 중",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(chan)
        }

        // 포그라운드 알림
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("수면 측정 중")
            .setContentText("수면 데이터를 전송합니다.")
            .setSmallIcon(R.drawable.ic_sleep_phony)
            .build()
        startForeground(FOREGROUND_ID, notif)

        // PARTIAL_WAKE_LOCK -> CPU 유지
        val pm = getSystemService(PowerManager::class.java)
        wakeLock = pm.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "Sleephony:MeasurementLock"
        ).apply { acquire() }

        serviceScope.launch {
            initializeSoundMap()
            while (isActive) {
                fetchAndUpload()
                delay(INTERVAL_MS)
            }
        }
    }

    private fun playSoundForSleepStage(stage: String) {
        val soundPath = soundMap[stage] ?: run {
        Log.e("ERR", "사운드가 존재하지 않음: $stage")
        return
        }

        val soundFile = File(soundPath)
        if(!soundFile.exists()) {
            Log.e("ERR", "파일이 존재하지 않음: $soundPath")
        }

        if(::mediaPlayer.isInitialized) {
            if(mediaPlayer.isPlaying) mediaPlayer.stop()
            mediaPlayer.reset()
        } else {
            mediaPlayer = MediaPlayer()
        }

        try {
            mediaPlayer.setDataSource(soundPath)
            mediaPlayer.prepare()
            mediaPlayer.isLooping = true
            mediaPlayer.start()
            Log.d("DBG", "사운드 재생 중:$stage - $soundPath")
        } catch (e: Exception) {
            Log.e("ERR", "재생 중 오류", e)
        }
    }

    private suspend fun fetchAndUpload() {
        Log.d("DBG", "측정 중입니다.")
        // 워치에서 데이터 가져오기

        // 더미데이터 활용

        val inputStream = resources.openRawResource(R.raw.biodatadummy)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        // 서버에 데이터 보내기
        val currentInstant = Clock.System.now()
        val currentZone = TimeZone.currentSystemDefault()
        val localDateTime = currentInstant.toLocalDateTime(currentZone)

        val gson = Gson()
        val req = gson.fromJson(jsonString, SleepBioDataRequest::class.java).copy(
            measuredAt = "$localDateTime"
        )

        val result = measurementRepository.sleepMeasurement(req)
        result
            .onSuccess { bioResult ->
                Log.d("DBG", "측정 레벨: ${bioResult.level}, 점수: ${bioResult.score}")
                playSoundForSleepStage(bioResult.level)

                if (mode == AlarmMode.COMFORT){
                    val now = System.currentTimeMillis()
                    val inWindow = now in startTimestamp..endTimestamp
                    val reached = bioResult.level == targetStage
                    val timeout = now >= endTimestamp

                    if ((inWindow && reached) || timeout) {
                        triggerAlarm()
                        serviceScope.cancel()
                    }
                }
            }
            .onFailure { err ->
                Log.e("ERR", "수면 데이터 전송 실패", err)
            }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra("mode")?.let {
            mode = AlarmMode.valueOf(it)
        }
        if (mode == AlarmMode.COMFORT) {
            startTimestamp = intent?.getLongExtra("startTimestamp", Long.MIN_VALUE) ?: Long.MIN_VALUE
            endTimestamp = intent?.getLongExtra("endTimestamp", Long.MAX_VALUE) ?: Long.MAX_VALUE
        }
        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("DBG", "수면 측정 중단")
        serviceScope.cancel()
        if(wakeLock.isHeld) wakeLock.release()
        if(::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        unregisterReceiver(sensorDataReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun triggerAlarm() {
        sendBroadcast(Intent(this, AlarmReceiver::class.java))
    }
}