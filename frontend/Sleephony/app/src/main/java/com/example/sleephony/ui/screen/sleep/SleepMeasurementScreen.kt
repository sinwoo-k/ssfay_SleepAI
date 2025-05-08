package com.example.sleephony.ui.screen.sleep

import ShootingStar
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sleephony.R
import com.example.sleephony.domain.model.AlarmMode

@Composable
fun SleepMeasurementScreen(
    viewModel: SleepViewModel = hiltViewModel()
){
    val settingData by viewModel.settingData.collectAsState()
    val (hour, minute, isAm, mode) = settingData

    Log.d("DBG", "설정 됐냐? $hour, $minute, $isAm, $mode")

    // 알람 범위 계산
    fun formatWithPeriod(totalMinutes: Int): String {
        val normalized = (totalMinutes + 24 * 60) % (24 * 60)
        val h24 = normalized / 60
        val m   = normalized % 60
        val period = if (h24 < 12) "오전" else "오후"
        val h12 = (h24 % 12).let { if (it == 0) 12 else it }
        return "$period ${h12}:${m.toString().padStart(2, '0')}"
    }

    // 기준을 분으로 환산
    val baseTotal = ((if (hour == 12) 0 else hour) + if (isAm) 0 else 12) * 60 + minute
    val startTotal = baseTotal - 30
    val endTotal   = baseTotal + 30

    val comfortRangeText = "${formatWithPeriod(startTotal)} – ${formatWithPeriod(endTotal)}"

    val alarmText = when(mode){
        AlarmMode.COMFORT -> comfortRangeText
        AlarmMode.EXACT -> "${if(isAm) "오전" else "오후"} $hour : ${minute.toString().padStart(2, '0')}"
        AlarmMode.NONE -> "알람이 없습니다."
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF182741),
                        Color(0xFF314282),
                        Color(0xFF1D1437)
                    )
                )
            )
    ) {
        ShootingStar(
            modifier = Modifier.fillMaxSize(),
            delayMillis = 200,
            durationMillis = 1000,
            startXFrac = 0.1f, startYFrac = 0.2f,
            endXFrac   = 0.5f, endYFrac   = 0.6f
        )
        ShootingStar(
            modifier = Modifier.fillMaxSize(),
            delayMillis = 1500,
            durationMillis = 1000,
            startXFrac = 0.2f, startYFrac = 0.15f,
            endXFrac   = 0.9f, endYFrac   = 0.7f
        )
        ShootingStar(
            modifier = Modifier.fillMaxSize(),
            delayMillis = 1000,
            durationMillis = 1000,
            startXFrac = 0.4f, startYFrac = 0.05f,
            endXFrac   = 0.9f, endYFrac   = 0.4f
        )
        Image(
            painter = painterResource(R.drawable.bg_stars),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.FillWidth
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 재생 중 음악
            Box(){}
            // 알람 시간
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_sleep_phony),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Text(
                    text = alarmText,
                    style = TextStyle(fontSize = 24.sp, color = Color.White)
                )
                Text(
                    text = when (mode) {
                        AlarmMode.COMFORT -> "수면 패턴에 맞춰 편안하게 기상합니다."
                        AlarmMode.EXACT   -> "정해진 시간에 정확히 기상합니다."
                        AlarmMode.NONE    -> "알람 없이 수면만 측정합니다."
                    },
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            // 중단 버튼
            Box {  }
        }
    }
}