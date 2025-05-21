package com.example.sleephony.ui.screen.report.component.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sleephony.ui.screen.report.viewmodel.ReportViewModel
import java.time.LocalDate

@Composable
fun AiReportPrompt(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate
) {
    val viewModel: ReportViewModel = hiltViewModel()
    val aiReportText by viewModel.aiReportText.collectAsState()
    val isLoading by viewModel.isLoadingAiReport.collectAsState(initial = true)

    // 서버에서 보고서를 가져오기
    LaunchedEffect(selectedDate) {
        viewModel.getAiReport(selectedDate.toString())
    }

    val expanded = remember { mutableStateOf(false) }

    // 로딩 상태에 따라 표시할 텍스트 설정
    val displayText = when {
        isLoading -> {
            "요청중입니다" // 로딩 중
        }
        aiReportText.isNotBlank() -> {
            if (expanded.value) aiReportText else aiReportText.take(100) + "..."
        }
        else -> {
            "응답이 없습니다" // aiReportText가 비어있을 경우
        }
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

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                // 로딩 중일 때만 로딩 표시
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Text(
                    text = displayText,
                    fontSize = 26.sp,
                    color = Color.White,
                    lineHeight = 36.sp
                )
            }
        }

        // 내용이 있고 로딩 중이지 않으면 "자세히 읽기" 버튼 표시
        if (!isLoading && aiReportText.isNotBlank()) {
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
