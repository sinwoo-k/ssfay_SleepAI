/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.sleephony.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.sleephony.components.HomeScreen
import com.example.sleephony.components.alarm.SetAlarmScreen
import com.example.sleephony.components.alarm.SleepingScreen
import com.example.sleephony.presentation.theme.Sleephony_wearTheme
import com.example.sleephony.presentation.theme.backGroundGeadientColor
import com.example.sleephony.viewmodel.AlarmViewModel

class MainActivity : ComponentActivity() {

    private lateinit var initialRoute: String
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        val alarmViewModel: AlarmViewModel by viewModels()
        initialRoute = intent.getStringExtra("start_destination") ?: "homeScreen"
        intentHandler(
            intent = intent,
            viewModel = alarmViewModel
        )


        // 런타임 권한 요청 등록
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(this, "심박수 측정을 위해 센서 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 권한이 없으면 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.BODY_SENSORS)
        }

        setContent {

            Sleephony_wearTheme {
                val navController = rememberSwipeDismissableNavController()

                SwipeDismissableNavHost(
                    navController = navController,
                    modifier = Modifier.background(Brush.verticalGradient(colors = backGroundGeadientColor)),
                    startDestination = initialRoute
                ) {
                    composable("homeScreen") {
                        HomeScreen(
                            navController = navController,
                            modifier = Modifier,
                            viewModel = alarmViewModel
                        )
                    }
                    composable("setalarm") {
                        SetAlarmScreen(
                            modifier = Modifier,
                            navController = navController,
                            viewModel = alarmViewModel
                        )
                    }
                    composable("sleepingscreen") {
                        SleepingScreen(
                            modifier = Modifier,
                            viewModel = alarmViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
        private fun intentHandler(
            intent : Intent,
            viewModel: AlarmViewModel
        ) {
            Log.d("ssafy","${intent.action}")
            intent?.let {
                if (it.action == "alarmOpen") {

                    val hour  = it.getIntExtra("hour",6)
                    val minute  = it.getIntExtra("minute",30)
                    val isAm  = it.getStringExtra("isAm") ?: ""
                    val alarmType = it.getStringExtra("alarmType") ?: ""
                    Log.d("ssafy","${isAm} ${hour} ${minute}")

                    val mode = when (alarmType) {
                        "COMFORT" -> {
                            "comfortable"
                        }
                        "EXACT" -> {
                            "normal"
                        }
                        "NONE" -> {
                            "none"
                        }
                        else -> {"normal"}
                    }

                    viewModel.wakeUpUpdate(hour = hour.toString(), minute = minute.toString(), meridiem = isAm)
                    viewModel.alarmTypeUpdate(mode)
                    initialRoute = "sleepingscreen"
                } else if ( it.action == "alarmCancel" ) {
                    initialRoute = "homeScreen"
                }
            }
        }
}
