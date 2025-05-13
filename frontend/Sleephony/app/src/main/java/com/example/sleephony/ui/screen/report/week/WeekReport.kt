package com.example.sleephony.ui.screen.report.week

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sleephony.R
import com.example.sleephony.ui.screen.report.components.AverageSleepScore
import com.example.sleephony.ui.screen.report.components.DetailSleep
import com.example.sleephony.ui.screen.report.components.SleepSummation
import com.example.sleephony.ui.screen.report.components.detail.Blue_text
import com.example.sleephony.ui.screen.report.components.detail.Comparison_text
import com.example.sleephony.ui.screen.report.components.detail.Gray_text
import com.example.sleephony.ui.screen.report.components.detail.White_text
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun WeekReport(
    modifier: Modifier,
    navController: NavController
) {
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val today = LocalDate.now()
    val weekStartState = remember { mutableStateOf(today.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))) }



    val days = remember { listOf("월", "화", "수", "목", "금", "토", "일") }
    val period = "week"

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            WeeklyCalendar(
                modifier = modifier.
                padding(0.dp,5.dp),
                weekStartState = weekStartState.value,
                onChange = { newDate -> weekStartState.value = newDate }
            )
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                Box() {
                    Column {
                        White_text("이번주")
                        Blue_text("평균 7시간 3분")
                        White_text("꿀잠을 유지하셨어요")
                        Gray_text("이대로만 계속 하세요!!")
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
                    sleepHours = listOf(7.5f, 6.8f, 8.2f, 7.0f, 6.5f, 8.5f, 9.0f),
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
                    sleepHours = listOf(5f, 14f, 8f, 21f, 11f, 6f, 8f),
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
                    sleepHours = listOf(1.6f, 1f, 1.3f, 1.4f, 1.3f, 1.8f, 1.2f),
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
                    sleepHours = listOf(4.8f, 4.5f, 3.8f, 3.6f, 3.4f, 4.2f, 3.9f),
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
                    sleepHours = listOf(1.5f, 1f, 1.2f, 1.4f, 1.3f, 1.8f, 1.4f),
                    path = "sleep_deep",
                    period = period
                )
            }
        }
    }
}