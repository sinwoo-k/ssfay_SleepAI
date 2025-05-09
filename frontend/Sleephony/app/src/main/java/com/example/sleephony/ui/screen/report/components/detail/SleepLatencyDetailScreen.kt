package com.example.sleephony.ui.screen.report.components.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sleephony.ui.screen.report.components.detail.sleepcycle.sleep_latency.ComparisonSleepLatency
import com.example.sleephony.ui.screen.report.components.detail.sleepcycle.sleep_latency.ComparisonSleepLatencyAverage
import com.example.sleephony.ui.screen.report.components.detail.sleepcycle.sleep_latency.ComparisonSleepLatencySleepTime
import com.example.sleephony.ui.screen.report.components.detail.sleepcycle.sleep_latency.ComparisonSleepLatencyWakeTime

@Composable
fun SleepLatencyDetailScreen(
    modifier: Modifier
) {
    val days = remember { listOf("월", "화", "수", "목", "금", "토", "일") }
    val sleepHours = remember { listOf(4f, 11f, 4f, 22f, 8f, 5f, 7f) }
    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                ComparisonSleepLatency(modifier = modifier, days, sleepHours)
                ComparisonSleepLatencyAverage(modifier = modifier)
                ComparisonSleepLatencySleepTime(modifier = modifier)
                ComparisonSleepLatencyWakeTime(modifier = modifier)
            }
        }
    }
}
