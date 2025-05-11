package com.example.sleephony.ui.screen.statistics.year

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sleephony.R
import com.example.sleephony.ui.screen.statistics.components.AverageSleepScore
import com.example.sleephony.ui.screen.statistics.components.DetailSleep
import com.example.sleephony.ui.screen.statistics.components.SleepSummation
import com.example.sleephony.ui.screen.statistics.components.detail.Blue_text
import com.example.sleephony.ui.screen.statistics.components.detail.Comparison_text
import com.example.sleephony.ui.screen.statistics.components.detail.Gray_text
import com.example.sleephony.ui.screen.statistics.components.detail.White_text
import com.example.sleephony.ui.screen.statistics.viewmodel.StatisticsViewModel
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun YearReport(
    modifier: Modifier,
    navController: NavController,
    statisticsViewModel: StatisticsViewModel
) {
    val today = LocalDate.now()
    val yearState = remember { mutableStateOf(today.with(TemporalAdjusters.firstDayOfYear())) }
    val year = yearState.value.year
    val days = remember { listOf("1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월") }
    val period = "year"

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            YearlyCalendar(
                modifier = modifier.
                padding(0.dp,5.dp),
                yearState = yearState.value,
                onChange = { newDate -> yearState.value = newDate }
            )
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                Box() {
                    Column {
                        White_text("${year}년 에는")
                        Blue_text("평균 7시간 3분")
                        Gray_text("꿀잠을 주무셨어요!!")
                    }
                }
                AverageSleepScore(modifier = modifier)
                SleepSummation(modifier = modifier)
                DetailSleep(
                    modifier = modifier,
                    navController = navController,
                    title = {
                        White_text(stringResource(R.string.sleep_time))
                    },
                    days = days,
                    sleepHours = listOf(7.5f, 6.8f, 8.2f, 7.0f, 6.5f,7.5f, 6.8f, 8.2f, 7.0f, 6.5f, 7.2f, 7.3f),
                    path = "sleep_time",
                    period = period
                )
                DetailSleep(
                    modifier = modifier,
                    navController = navController,
                    title={
                        White_text(stringResource(R.string.sleep_latency_time))
                        Comparison_text(blue_text = "1시간 13분", white_text = "이에요" )
                    },
                    days = days,
                    sleepHours = listOf(5f, 14f, 8f, 21f, 11f,5f, 14f, 8f, 21f, 11f,5f, 14f),
                    path = "sleep_latency",
                    period = period
                )
                DetailSleep(
                    modifier = modifier,
                    navController = navController,
                    title={
                        White_text(stringResource(R.string.average_REM_sleep))
                        Comparison_text(blue_text = "1시간 37분", white_text = "이에요")
                    },
                    days = days,
                    sleepHours = listOf(1.6f, 1f, 1.3f, 1.4f, 1.3f,1.6f, 1f, 1.3f, 1.4f, 1.3f,1.6f, 1f,),
                    path = "sleep_REM",
                    period = period
                )
                DetailSleep(
                    modifier = modifier,
                    navController = navController,
                    title={
                        White_text(stringResource(R.string.average_light_sleep))
                        Comparison_text(blue_text = "4시간 43분", white_text = "이에요")
                    },
                    days = days,
                    sleepHours = listOf(4.8f, 4.5f, 3.8f, 3.6f, 3.4f,4.8f, 4.5f, 3.8f, 3.6f, 3.4f,4.8f, 4.5f),
                    path = "sleep_light",
                    period = period
                )
                DetailSleep(
                    modifier = modifier,
                    navController = navController,
                    title={
                        White_text(stringResource(R.string.average_deep_sleep))
                        Comparison_text(blue_text = "1시간 13분", white_text = "이에요")
                    },
                    days = days,
                    sleepHours = listOf(1.5f, 1f, 1.2f, 1.4f, 1.3f,1.5f, 1f, 1.2f, 1.4f, 1.3f,1.5f, 1f),
                    path = "sleep_deep",
                    period = period
                )
            }
        }
    }
}