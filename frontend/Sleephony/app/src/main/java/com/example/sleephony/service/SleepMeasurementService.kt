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
import com.example.sleephony.BuildConfig
import com.example.sleephony.R
import com.example.sleephony.data.datasource.local.ThemeLocalDataSource
import com.example.sleephony.data.datasource.remote.measurement.SleepStageSSEClient
import com.example.sleephony.data.model.measurement.SleepBioDataRequest
import com.example.sleephony.data.model.theme.SoundDto
import com.example.sleephony.domain.model.AlarmMode
import com.example.sleephony.domain.repository.MeasurementRepository
import com.example.sleephony.domain.repository.ThemeRepository
import com.example.sleephony.receiver.AlarmReceiver
import com.example.sleephony.utils.TokenProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.json.JSONObject
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
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var wakeLock: PowerManager.WakeLock

    private lateinit var sseClient: SleepStageSSEClient

    @Inject lateinit var measurementRepository: MeasurementRepository
    @Inject lateinit var themeRepository: ThemeRepository
    @Inject lateinit var themeLocalDataSource: ThemeLocalDataSource
    @Inject lateinit var tokenProvider: TokenProvider

    private lateinit var mediaPlayer: MediaPlayer

    private var lastSleepStage: String? = null

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

    private val accelBuffer = mutableListOf<List<Double>>()
    private val hrBuffer = mutableListOf<Double>()
    private val tempBuffer = mutableListOf<Double>()

    // 센서 데이터 받아오기
    private val sensorDataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val data = intent?.getStringExtra("sensorData") ?: return
            Log.d("DBG", "Wear OS 센서 데이터: $data")

           try {
               val obj = JSONObject(data)

               val accelList = obj.getJSONArray("accelerometer")
               val hrList = obj.getJSONArray("hearRate")
               val tempList = obj.getJSONArray("temperature")
               for (i in 0 until accelList.length()) {
                   val accelStr = accelList.getString(i)
                   val values = listOf(accelStr.split(",").map { it.trim().toDouble() })
                   accelBuffer.addAll(values)
                   if (i<150) {
                       val hrStr = hrList.getString(i).toDouble()
                       val hrTempList = mutableListOf<Double>()
                       repeat(20) { hrTempList.add(hrStr) }
                        hrBuffer.addAll(hrTempList)

                       val temStr = tempList.getString(i).toDouble()
                       val tempTempList = mutableListOf<Double>()
                       repeat(20) { tempTempList.add(temStr) }
                       tempBuffer.addAll(tempTempList)
                   }
               }

               if (accelBuffer.size == 3000) {
                   serviceScope.launch {
                       fetchAndUpload()
                   }
               }


           } catch(e:Exception) {
               Log.e("ERR", "센서 데이터 전송 실패", e)
           }
        }
    }

    override fun onCreate() {
        super.onCreate()

        // sse 초기화
        val token = tokenProvider.getToken().orEmpty()
        Log.d("SSE", "토큰 확인 $token")
        sseClient = SleepStageSSEClient(
            baseUrl = BuildConfig.SLEEPHONY_BASE_URL,
            token = token
        ).apply {
            setListener(object : SleepStageSSEClient.Listener{
                override fun onOpen() {
                    Log.d("SSE", "SSE 연결 성공")
                }

                override fun onSleepStageReceived(requestId: String?, sleepStage: String) {
                    serviceScope.launch(Dispatchers.Main) {
                        Log.d("SSE", "응답 성공: $sleepStage")
                        if (sleepStage != lastSleepStage) {
                            playSoundForSleepStage(sleepStage)
                            lastSleepStage = sleepStage
                        } else {
                            Log.d("SSE", "동일 단계($sleepStage) 재생 생략")
                        }

                        if (mode == AlarmMode.COMFORT){
                            val now = System.currentTimeMillis()
                            val inWindow = now in startTimestamp..endTimestamp
                            val reached = sleepStage == targetStage
                            val timeout = now >= endTimestamp

                            if ((inWindow && reached) || timeout) {
                                triggerAlarm()
                                serviceScope.cancel()
                            }
                        }

                    }
                }

                override fun onError(message: String) {
                    Log.e("SSE", message)
                }

                override fun onComplete() {
                    Log.d("SSE", "SSE stream completed")
                }
            })
        }


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

            // 처음엔 AWAKE로 재생
            playSoundForSleepStage("AWAKE")
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

        // 볼륨 크기
        fun volumeFor(stage: String): Float = when (stage) {
            "AWAKE" -> 1.0f   // 깨어있을 땐 최대
            "NREM1" -> 0.5f   // 살짝 낮춤
            "NREM2" -> 0.3f   // 절반 정도
            "REM"   -> 0.1f   // 매우 작게
            else    -> 1.0f
        }

        try {
            mediaPlayer.setDataSource(soundPath)
            mediaPlayer.prepare()

            // 사운드 조절
            val vol = volumeFor(stage)
            Log.d("SOUND", "$vol")
            mediaPlayer.setVolume(vol, vol)
            // 반복 재생
            mediaPlayer.isLooping = true
            mediaPlayer.start()
            Log.d("SOUND", "사운드 재생 중:$stage - $soundPath")
        } catch (e: Exception) {
            Log.e("ERR", "재생 중 오류", e)
        }
    }

    private suspend fun fetchAndUpload() {
        Log.d("Measurement", "측정 준비 중")
        // 워치에서 데이터 정제
        val accX = accelBuffer.map { it[0] }
        val accY = accelBuffer.map { it[1] }
        val accZ = accelBuffer.map { it[2] }

        val hrList = hrBuffer.toList()
        val tempList = tempBuffer.toList()

        accelBuffer.clear()
        hrBuffer.clear()
        tempBuffer.clear()

        val currentInstant = Clock.System.now()
        val currentZone = TimeZone.currentSystemDefault()
        val localDateTime = currentInstant.toLocalDateTime(currentZone)

        // 서버에 데이터 보내기
        val req = SleepBioDataRequest(
            accX = accX,
            accY = accY,
            accZ = accZ,
            hr = hrList,
            temp = tempList,
            measuredAt = localDateTime.toString()
        )
        if (accX.size == 3000) {
            Log.d("Measurement", "데이터 전송")

            withContext(Dispatchers.IO) {
                var json = Gson().toJson(req)

                val fileName = "sleep_req+${System.currentTimeMillis()}.txt"
                val file = File(applicationContext.filesDir, fileName)

                file.writeText(json)
            }
            sseClient.connect(req)
        } else {
            Log.d("Measurement", "데이터 수 부족 ${accX.size}")
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
        sseClient.disconnect()
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