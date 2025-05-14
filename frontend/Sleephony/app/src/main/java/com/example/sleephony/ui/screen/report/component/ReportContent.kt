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
        if (report != null) {
            val totalSleep = report.remMinutes + report.lightMinutes + report.deepMinutes
            val sleepDurationText = "${totalSleep / 60}시간 ${totalSleep % 60}분"

            val prevSleep = reportResult?.previousTotalSleepMinutes ?: 0
            val diff = totalSleep - prevSleep
            val diffText = when {
                kotlin.math.abs(diff) <= 30 -> "전날과 비슷한 수면 시간이네요!"
                diff > 0 -> "전날보다 ${diff}분 충전하셨어요!!"
                else -> "전날보다 ${-diff}분 부족했어요!"
            }

            val comment = when {
                report.sleepScore >= 85 -> "꿀잠을 유지하셨어요"
                report.sleepScore >= 60 -> "꽤 잘 주무셨어요"
                else -> "피로가 남아있을 수 있어요"
            }

            SleepScoreSection(
                sleepDurationText = sleepDurationText,
                diffText = diffText,
                comment = comment
            )

            val remPercent = (report.remMinutes * 100f / totalSleep).toInt().coerceAtMost(100)
            val lightPercent = (report.lightMinutes * 100f / totalSleep).toInt().coerceAtMost(100)
            val deepPercent = (report.deepMinutes * 100f / totalSleep).toInt().coerceAtMost(100)

            AverageSleepScore(
                modifier = Modifier.fillMaxWidth(),
                averageSleepScore = report.sleepScore,
                averageSleepTimeMinutes = totalSleep + report.awakeMinutes,
                averageSleepLatencyMinutes = report.awakeMinutes
            )

            SleepSummationGraph(
                modifier = Modifier.fillMaxWidth(),
                averageSleepLatencyMinutes = report.awakeMinutes.toFloat(),
                averageRemSleepMinutes = report.remMinutes.toFloat(),
                averageRemSleepPercentage = remPercent,
                averageLightSleepMinutes = report.lightMinutes.toFloat(),
                averageLightSleepPercentage = lightPercent,
                averageDeepSleepMinutes = report.deepMinutes.toFloat(),
                averageDeepSleepPercentage = deepPercent,
                averageSleepCycleCount = report.sleepCycles,
                viewModel = reportViewModel
            )
        }
    }
}
