package com.example.sleephony

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.sleephony.domain.model.AlarmMode
import com.example.sleephony.navigation.AppNavGraph
import com.example.sleephony.service.AlarmForegroundService
import com.example.sleephony.service.SleepMeasurementService
import com.example.sleephony.ui.screen.sleep.SleepViewModel
import com.example.sleephony.ui.theme.SleephonyTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        var keyHash = Utility.getKeyHash(this)
        Log.d("DBG", "키해시 검사 : $keyHash")

    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var initialRoute: String
    private val sleepViewModel : SleepViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialRoute = intent.getStringExtra("start_destination") ?: "splash"

        handleIntent(intent, sleepViewModel)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            SleephonyTheme {
                val sysUi = rememberSystemUiController()
                SideEffect {
                    // StatusBar 투명 + 아이콘 흰색(=darkIcons=false)
                    sysUi.setStatusBarColor(
                        color = Color(0xFF182741),
                        darkIcons = false
                    )
                    // NavigationBar 투명 + 아이콘 흰색
                    sysUi.setNavigationBarColor(
                        color = Color(0xFF182741),
                        darkIcons = false
                    )
                }

                // 내비게이션 관련
                val navController = rememberNavController()

                AppNavGraph(
                    navController = navController,
                    startDestination = initialRoute,
                    vm = sleepViewModel
                )
            }
        }
    }
    private fun handleIntent(
        intent : Intent,
        viewModel: SleepViewModel
    ) {
        Log.d("ssafy","${intent.action}")
        intent?.let {
            if (it.action == "alarmOpen") {

                val hour  = it.getIntExtra("hour",6)
                val minute  = it.getIntExtra("minute",30)
                val isAm  = it.getBooleanExtra("isAm",false)
                val alarmType = it.getStringExtra("alarmType") ?: ""
                Log.d("ssafy","${isAm} ${hour} ${minute}")

                val mode = when (alarmType) {
                    "comfortable" -> {
                        AlarmMode.COMFORT
                    }
                    "normal" -> {
                        AlarmMode.EXACT
                    }
                    "none" -> {
                        AlarmMode.NONE
                    }
                    else -> {AlarmMode.EXACT}
                }

                viewModel.onTimeChanged(hour = hour, minute = minute, isAm = isAm)
                viewModel.onModeSelected(mode)
                initialRoute = "sleep_setting"
                viewModel.onStartClicked()
            } else if ( it.action == "alarmCancel" ) {
                initialRoute = "sleep_setting"
                viewModel.onStopClicked()
                stopService(Intent(this,AlarmForegroundService::class.java))
                val close = Intent("alarmClose")
                sendBroadcast(close)
                stopService(Intent(this, AlarmForegroundService::class.java))
                stopService(Intent(this, SleepMeasurementService::class.java))
            }
        }
    }
}

