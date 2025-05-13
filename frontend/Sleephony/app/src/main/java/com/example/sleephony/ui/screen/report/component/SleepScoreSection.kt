package com.example.sleephony.ui.screen.report.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R

@Composable
fun SleepScoreSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Optional padding
    ) {
        // 수면 설명 문구
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "어제 밤",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "총 7시간 15분",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.SkyBlue)
            )
            Text(
                text = "꿀잠을 유지하셨어요",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "전날보다 29분 충전하셨네요!!",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.3f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
