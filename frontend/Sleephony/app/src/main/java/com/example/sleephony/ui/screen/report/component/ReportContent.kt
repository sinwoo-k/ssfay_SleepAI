package com.example.sleephony.ui.screen.report.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sleephony.ui.screen.report.viewmodel.ReportViewModel
import com.example.sleephony.ui.screen.statistics.components.AverageSleepScore
import java.time.LocalDate
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun ReportContent(
    modifier: Modifier = Modifier,
    reportViewModel: ReportViewModel,
    selectedDate: LocalDate
) {
    val todayStr = selectedDate.toString()
    val reportResult = reportViewModel.sleepReport.collectAsState().value
    val report = reportResult?.today
    val scrollState = rememberScrollState()

    LaunchedEffect(selectedDate) {
        reportViewModel.getSleepReport(todayStr)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 수면 데이터
        val rem = report?.remMinutes ?: 0
        val light = report?.lightMinutes ?: 0
        val deep = report?.deepMinutes ?: 0
        val awake = report?.awakeMinutes ?: 0
        val cycles = report?.sleepCycles ?: 0
        val score = report?.sleepScore ?: 0
        val totalSleep = rem + light + deep

        // 수면 단계 퍼센트 계산
        val remPercent = if (totalSleep > 0) (rem * 100f / totalSleep).toInt().coerceAtMost(100) else 0
        val lightPercent = if (totalSleep > 0) (light * 100f / totalSleep).toInt().coerceAtMost(100) else 0
        val deepPercent = if (totalSleep > 0) (deep * 100f / totalSleep).toInt().coerceAtMost(100) else 0

        // 데이터가 없을 경우 처리
        if (report == null) {
            Text(
                text = "선택된 날짜에는 \n 수면 데이터가 없습니다.",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(20.dp))
                    .padding(16.dp), // 안쪽 여백
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = .7f),
                textAlign = TextAlign.Center, // 가운데 정렬
            )

        } else {

            val comment = "주무셨어요"

            val diffText = when {
                report.sleepScore >= 85 -> "평소보다 깊은 잠을 주무셨어요"
                report.sleepScore >= 60 -> "꽤 잘 주무신거 같아요"
                else -> "피로가 남아있을 수 있어요"
            }

            val sleepDurationText = "${totalSleep / 60}시간 ${totalSleep % 60}분"

            SleepScoreSection(
                sleepDurationText = sleepDurationText,
                diffText = diffText,
                comment = comment
            )
        }

        // 점수 영역
        AverageSleepScore(
            modifier = Modifier.fillMaxWidth(),
            averageSleepScore = score,
            averageSleepTimeMinutes = totalSleep + awake,
            averageSleepLatencyMinutes = awake
        )

        // 그래프 영역, selectedDate를 전달
        SleepSummationGraph(
            modifier = Modifier.fillMaxWidth(),
            averageSleepLatencyMinutes = if (report == null) 0f else awake.toFloat(),
            averageRemSleepMinutes = if (report == null) 0f else rem.toFloat(),
            averageRemSleepPercentage = if (report == null) 0 else remPercent,
            averageLightSleepMinutes = if (report == null) 0f else light.toFloat(),
            averageLightSleepPercentage = if (report == null) 0 else lightPercent,
            averageDeepSleepMinutes = if (report == null) 0f else deep.toFloat(),
            averageDeepSleepPercentage = if (report == null) 0 else deepPercent,
            averageSleepCycleCount = if (report == null) 0 else cycles,
            viewModel = reportViewModel,
            selectedDate = selectedDate
        )
    }
}