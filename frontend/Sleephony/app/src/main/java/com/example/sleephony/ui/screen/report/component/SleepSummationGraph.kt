package com.example.sleephony.ui.screen.report.component

import com.example.sleephony.ui.screen.statistics.components.parsingTime
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.report.screen.SleepStageGraph
import com.example.sleephony.ui.screen.report.viewmodel.ReportViewModel
import java.time.LocalDate

@Composable
fun SleepSummationGraph(
    modifier: Modifier = Modifier,
    averageSleepLatencyMinutes: Float,
    averageRemSleepMinutes: Float,
    averageRemSleepPercentage: Int,
    averageLightSleepMinutes: Float,
    averageLightSleepPercentage: Int,
    averageDeepSleepMinutes: Float,
    averageDeepSleepPercentage: Int,
    averageSleepCycleCount: Int,
    viewModel: ReportViewModel,
    selectedDate: LocalDate
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.sleep_summation),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .background(Color.Black.copy(alpha = .3f), RoundedCornerShape(20.dp))
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                SleepStageGraph(
                    viewModel = viewModel,
                    selectedDate = selectedDate
                )

                SleepSummaryRow(
                    label = stringResource(R.string.non_sleep),
                    time = parsingTime(averageSleepLatencyMinutes.toInt()),
                    color = colorResource(R.color.sand)
                )
                SleepSummaryRow(
                    label = stringResource(R.string.REM_sleep),
                    time = "${parsingTime(averageRemSleepMinutes.toInt())} (${averageRemSleepPercentage}%)",
                    color = colorResource(R.color.purple)
                )
                SleepSummaryRow(
                    label = stringResource(R.string.light_sleep),
                    time = "${parsingTime(averageLightSleepMinutes.toInt())} (${averageLightSleepPercentage}%)",
                    color = colorResource(R.color.indigo)
                )
                SleepSummaryRow(
                    label = stringResource(R.string.deep_sleep),
                    time = "${parsingTime(averageDeepSleepMinutes.toInt())} (${averageDeepSleepPercentage}%)",
                    color = colorResource(R.color.deep_sea_blue)
                )
                SleepSummaryRow(
                    label = stringResource(R.string.sleep_cycle),
                    time = "${averageSleepCycleCount}회",
                    color = colorResource(R.color.light_gray)
                )
            }
        }
    }
}

@Composable
private fun SleepSummaryRow(label: String, time: String, color: Color) {
    val alphaWhite = Color.White.copy(alpha = .7f)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(color = color, shape = RoundedCornerShape(100.dp))
            )
            Text(text = label, fontSize = 20.sp, color = alphaWhite)
        }
        Text(text = time, fontSize = 20.sp, color = alphaWhite)
    }
}