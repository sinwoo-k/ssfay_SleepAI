package com.example.sleephony.ui.screen.sleep

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sleephony.R
import com.example.sleephony.domain.model.AlarmMode
import com.example.sleephony.ui.common.component.TimeWheelPicker

@Composable
fun SleepSettingScreen(
    onStart: () -> Unit,
    viewModel: SleepViewModel = hiltViewModel()
) {
    val settingData by viewModel.settingData.collectAsState()
    val (hour, minute, isAm, mode) = settingData
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

    Box(modifier = Modifier.fillMaxSize()) {
        // 1) 그라데이션 배경
        Box(modifier = Modifier
            .matchParentSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF182741),
                        Color(0xFF314282),
                        Color(0xFF1D1437)
                    )
                )
            )
        )

        // 2) 상단 별 이미지
        Image(
            painter = painterResource(R.drawable.bg_stars),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.FillWidth
        )

        // 3) UI 오버레이
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {

            Spacer(Modifier.height(48.dp))

            // 3-2) 시간 휠
            Column(modifier = Modifier.padding(16.dp)) {
                TimeWheelPicker (
                    initialHour   = hour,
                    initialMinute = minute,
                    initialIsAm   = isAm
                ) { h, m, am ->
                    viewModel.onTimeChanged(h, m, am)
                }

                Spacer(modifier = Modifier.height(24.dp))

            }


            Spacer(Modifier.height(48.dp))

            // 3-3) 알람 모드 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AlarmMode.values().forEach { m ->
                    val selected = m == mode
                    OutlinedButton(
                        onClick = { viewModel.onModeSelected(m) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selected) Color.White.copy(alpha = 0.2f) else Color.Transparent
                        ),
                        border = if (selected)
                            BorderStroke(2.dp, Color.White)
                        else
                            BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = m.label, color = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 3-4) 모드 설명
            Text(
                text = when (mode) {
                    AlarmMode.COMFORT -> "수면 패턴에 맞춰 편안하게 기상합니다.\n  예정 시간 : $comfortRangeText "
                    AlarmMode.EXACT   -> "정해진 시간에 정확히 기상합니다.\n"
                    AlarmMode.NONE    -> "알람 없이 수면만 측정합니다.\n"
                },
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(24.dp))

            // 3-5) 수면 시작 버튼
            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape =  RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5063D4))
            ) {
                Text(text = "수면 시작하기", color = Color.White, fontSize = 18.sp)
            }

            Spacer(Modifier.height(8.dp))

            // 3-6) 안내 문구
            Text(
                text = "원활한 측정을 위해 네트워크 연결이 필요합니다.",
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
