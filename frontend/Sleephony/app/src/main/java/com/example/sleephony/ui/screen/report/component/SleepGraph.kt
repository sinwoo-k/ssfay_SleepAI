package com.example.sleephony.ui.screen.report.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sleephony.ui.screen.report.component.SleepStageChart
import com.example.sleephony.ui.screen.report.viewmodel.ReportViewModel
import com.example.sleephony.utils.toSleepStageBlocks
import java.time.LocalDate

@Composable
fun SleepStageGraph(
    viewModel: ReportViewModel,
    selectedDate: LocalDate
) {
    val selectedDateStr = selectedDate.toString()
    val sleepGraph by viewModel.sleepGraphData.collectAsState()

    LaunchedEffect(selectedDate) {
        viewModel.getSleepGraph(selectedDateStr)
    }

    if (sleepGraph.isNotEmpty()) {
        val blocks = remember(sleepGraph) { sleepGraph.toSleepStageBlocks() }

        SleepStageChart(
            blocks = blocks,
            sleepStartTime = blocks.first().start,
            sleepEndTime = blocks.last().end,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}
