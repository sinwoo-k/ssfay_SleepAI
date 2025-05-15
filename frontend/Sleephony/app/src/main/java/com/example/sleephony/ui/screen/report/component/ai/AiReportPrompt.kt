package com.example.sleephony.ui.screen.report.component.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AiReportPrompt(modifier: Modifier = Modifier) {
    val expanded = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Black.copy(alpha = .3f),
                shape = RoundedCornerShape(30.dp)
            )
            .padding(20.dp)
    ) {
        Text(
            text = "AI 의 수면 분석",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        val fullText = "3일간 수면이 평균 4시간 34분으로 매우 부족해요. 수면시간과 깊은 잠이 부족하면 혈당을 떨어뜨리는 인슐린을 효과적으로 사용하지 못하는 상태가 되어 인슐린 저항성이 증가하고 당뇨병 위험성이 5배 이상 높아져요. 깊은 잠을 증가시키기 위해서는 ...."
        val shortText = fullText.take(70) + "..."

        Text(
            text = if (expanded.value) fullText else shortText,
            fontSize = 26.sp,
            color = Color.White,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (expanded.value) "접기 ▲" else "자세히 읽기 >",
            fontSize = 20.sp,
            color = Color(0xFF8FB5FF),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { expanded.value = !expanded.value },
            textAlign = TextAlign.Start
        )
    }
}
