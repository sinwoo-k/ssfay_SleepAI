/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.sleepphony_wear_os.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.sleephony_wear.components.HomeScreen
import com.example.sleephony_wear.components.alarm.SetAlarmScreen
import com.example.sleephony_wear.screens.SleepAlarmScreen
import com.example.sleepphony_wear_os.presentation.theme.Sleephony_wearTheme
import com.example.sleepphony_wear_os.presentation.theme.backGroundGeadientColor

class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

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

        // Compose UI 시작
        setContent {
            Sleephony_wearTheme {
                val navController = rememberSwipeDismissableNavController()

                val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
                val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)

                SwipeDismissableNavHost(
                    navController = navController,
                    modifier = Modifier.background(Brush.verticalGradient(colors = backGroundGeadientColor)),
                    startDestination = "homeScreen"
                ) {
                    composable("homeScreen") {
                        HomeScreen(
                            navController = navController,
                            modifier = Modifier
                        )
                    }
                    composable("setalarm") {
                        SetAlarmScreen(
                            modifier = Modifier,
                            navController = navController
                        )
                    }
                    composable("sleepalarm") {
                        SleepAlarmScreen()
                    }
                    composable("sensorList") {
                        SensorListScreen(sensorList = sensorList)
                    }
                }
            }
        }
    }
}

@Composable
fun SensorListScreen(sensorList: List<Sensor>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "센서 리스트:")
        sensorList.forEach { sensor ->
            Text(text = "Name: ${sensor.name}, Type: ${sensor.type}", modifier = Modifier.padding(top = 8.dp))
        }
    }
}