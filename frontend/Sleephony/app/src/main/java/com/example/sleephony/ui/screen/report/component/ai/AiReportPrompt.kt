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
fun AiReportPrompt(
    fullText: String,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }

    val isEmpty = fullText.isBlank()
    val displayText = if (isEmpty) {
        "해당 날짜에는 수면 데이터가 없어요. \n다른 날짜를 선택해 주세요."
    } else {
        if (expanded.value) fullText else fullText.take(100) + "..."
    }

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

        Text(
            text = displayText,
            fontSize = 26.sp,
            color = Color.White,
            lineHeight = 36.sp
        )

        if (!isEmpty) {
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
}
