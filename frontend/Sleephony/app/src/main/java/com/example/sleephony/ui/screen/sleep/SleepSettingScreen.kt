package com.example.sleephony.ui.screen.sleep

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sleephony.R
import com.example.sleephony.domain.model.AlarmMode

@Composable
fun SleepSettingScreen() {
    // 임시 고정 값
    val themeLabel = "빗속으로"
    val hour = 6
    val minute = 30
    val mode = AlarmMode.COMFORT

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

            // 3-2) 시간 휠 (간단히 텍스트로 배치)
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = String.format("%02d:%02d", (hour + 23) % 24, minute),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = String.format("%02d:%02d", hour, minute),
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = String.format("%02d:%02d", (hour + 1) % 24, minute),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
                // 클릭 영역
                Box(
                    Modifier
                        .matchParentSize()
                        .clickable { /*TODO: TimePickerDialog 띄우기*/ }
                )
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
                        onClick = { /*TODO: mode 변경*/ },
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
                    AlarmMode.COMFORT -> "수면 패턴에 맞춰 편안하게 기상합니다."
                    AlarmMode.EXACT   -> "정해진 시간에 정확히 기상합니다."
                    AlarmMode.NONE    -> "알람 없이 수면만 측정합니다."
                },
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.weight(1f))

            // 3-5) 수면 시작 버튼
            Button(
                onClick = { /*TODO: 수면 시작 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5063D4))
            ) {
                Text(text = "수면 시작하기", color = Color.White)
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
