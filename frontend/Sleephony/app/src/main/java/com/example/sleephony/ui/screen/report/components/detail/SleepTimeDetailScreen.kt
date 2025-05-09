package com.example.sleephony.ui.screen.report.components.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sleephony.ui.screen.report.components.detail.sleepcycle.sleeptime.ComparisonAverageSleepTime
import com.example.sleephony.ui.screen.report.components.detail.sleepcycle.sleeptime.ComparisonBefore
import com.example.sleephony.ui.screen.report.components.detail.sleepcycle.sleeptime.ComparisonMost
import com.example.sleephony.ui.screen.report.components.detail.sleepcycle.sleeptime.ComparisonSleep

@Composable
fun SleepTimeDetailScreen(
    modifier: Modifier
) {
    val days = remember { listOf("월", "화", "수", "목", "금", "토", "일") }
    val sleepHours = remember { listOf(7.5f, 6.8f, 8.2f, 7.0f, 6.5f, 8.5f, 9.0f) }
    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                ComparisonAverageSleepTime(modifier = modifier, days = days, sleepHours = sleepHours)
                ComparisonSleep(modifier = modifier, )
                ComparisonBefore(modifier = modifier)
                ComparisonMost(modifier = modifier)
            }
        }
    }
}
