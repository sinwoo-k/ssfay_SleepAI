package com.example.sleephony_wear.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sleepphony_wear_os.service.SleepSensorService
import kotlinx.coroutines.flow.MutableStateFlow

// 공유할 Flow 객체
val sensorDataFlow = MutableStateFlow<Map<String, String>>(emptyMap())
val isServiceRunning = MutableStateFlow(false)

@Composable
fun SleepAlarmScreen() {
    val context = LocalContext.current
    val sensorData by sensorDataFlow.collectAsStateWithLifecycle()
    val serviceRunning by isServiceRunning.collectAsStateWithLifecycle()

    // 서비스 시작/중지 함수
    val toggleService = {
        if (serviceRunning) {
            // 서비스 중지
            val intent = Intent(context, SleepSensorService::class.java)
            context.stopService(intent)
            isServiceRunning.value = false
        } else {
            // 서비스 시작
            val intent = Intent(context, SleepSensorService::class.java)
            context.startForegroundService(intent)
            isServiceRunning.value = true
        }
    }

    // 앱 시작 시 서비스 자동 시작
    LaunchedEffect(Unit) {
        if (!serviceRunning) {
            val intent = Intent(context, SleepSensorService::class.java)
            context.startForegroundService(intent)
            isServiceRunning.value = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상태 표시
        Text(
            text = if (serviceRunning) "센서 모니터링 활성화" else "센서 모니터링 중지됨",
            color = if (serviceRunning) Color.Green else Color.Red,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )

        // 시작/중지 버튼
        Button(
            onClick = toggleService,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (serviceRunning) Color.Red else Color.Green
            ),
            modifier = Modifier.padding(4.dp)
        ) {
            Text(if (serviceRunning) "중지" else "시작")
        }

        // 센서 데이터 목록
        LazyColumn(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                if (serviceRunning) {
                    Text(
                        "센서 데이터:",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    if (sensorData.isEmpty()) {
                        Text("데이터 수집 중...", color = Color.Gray)
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))
                        sensorData.forEach { (name, value) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                onClick = {}
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = value,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Text("센서 모니터링이 중지되었습니다.", color = Color.Gray)
                }
            }
        }
    }
}