package com.example.sleephony.ui.screen.report.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sleephony.ui.screen.report.component.SleepStageChart
import com.example.sleephony.ui.screen.report.viewmodel.ReportViewModel
import com.example.sleephony.utils.toSleepStageBlocks
import java.time.LocalDate

@Composable
fun SleepStageGraph(viewModel: ReportViewModel) {
    val sleepGraph by viewModel.sleepGraphData.collectAsState()

    LaunchedEffect(Unit) {
        val yesterday = LocalDate.now().minusDays(1).toString()
        viewModel.getSleepGraph(yesterday)
    }

    if (sleepGraph.isNotEmpty()) {
        val blocks = remember(sleepGraph) { sleepGraph.toSleepStageBlocks() }

        SleepStageChart(
            blocks = blocks,
            sleepStartTime = blocks.first().start,
            sleepEndTime = blocks.last().end,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    } else {
        Text(
            text = "수면 데이터 불러오는 중...",
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }
}
