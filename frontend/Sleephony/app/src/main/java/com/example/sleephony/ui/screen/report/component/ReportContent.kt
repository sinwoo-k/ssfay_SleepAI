package com.example.sleephony.ui.screen.report.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sleephony.ui.screen.report.viewmodel.ReportViewModel
import com.example.sleephony.ui.screen.statistics.components.AverageSleepScore
import java.time.LocalDate
import kotlin.math.abs

@Composable
fun ReportContent(
    modifier: Modifier = Modifier,
    reportViewModel: ReportViewModel
) {
    val todayStr = LocalDate.now().minusDays(1).toString()
    val reportResult = reportViewModel.sleepReport.collectAsState().value
    val report = reportResult?.today
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
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

        // SleepScoreSection
        if (report != null) {
            val prevSleep = reportResult?.previousTotalSleepMinutes ?: 0
            val diff = totalSleep - prevSleep
            val absDiffFormatted = formatMinutesToHourMinute(abs(diff))

            val diffText = when {
                abs(diff) <= 30 -> "전날과 비슷한 수면 시간이네요!"
                diff > 0 -> "전날보다 ${absDiffFormatted} 충전하셨어요!!"
                else -> "전날보다 ${absDiffFormatted} 부족했어요!"
            }

            val comment = when {
                report.sleepScore >= 85 -> "꿀잠을 유지하셨어요"
                report.sleepScore >= 60 -> "꽤 잘 주무셨어요"
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

        // 그래프 영역
        SleepSummationGraph(
            modifier = Modifier.fillMaxWidth(),
            averageSleepLatencyMinutes = awake.toFloat(),
            averageRemSleepMinutes = rem.toFloat(),
            averageRemSleepPercentage = remPercent,
            averageLightSleepMinutes = light.toFloat(),
            averageLightSleepPercentage = lightPercent,
            averageDeepSleepMinutes = deep.toFloat(),
            averageDeepSleepPercentage = deepPercent,
            averageSleepCycleCount = cycles,
            viewModel = reportViewModel
        )
    }
}

//분 단위를 시간+분 형식으로 변환하는 함수
fun formatMinutesToHourMinute(minutes: Int): String {
    val hours = minutes / 60
    val mins = minutes % 60
    return when {
        hours > 0 && mins > 0 -> "${hours}시간 ${mins}분"
        hours > 0 -> "${hours}시간"
        else -> "${mins}분"
    }
}
