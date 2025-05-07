package com.example.sleephony.ui.screen.report.week

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.report.components.AverageSleepScore
import com.example.sleephony.ui.screen.report.components.SleepSummation
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun WeekReport(
    modifier: Modifier
) {
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val today = LocalDate.now()
    val weekStartState = remember { mutableStateOf(today.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))) }
    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp)
            .fillMaxSize(),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                WeeklyCalendar(modifier = modifier,
                    firstDayOfWeek = firstDayOfWeek,
                    weekStartState = weekStartState.value,
                    onPre = { newDate -> weekStartState.value = newDate },
                    onNext = { newDate -> weekStartState.value = newDate } )
                Box() {
                    Column {
                        Text(text = "이번주", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text(text = "평균 7시간 3분", color = colorResource(R.color.SkyBlue), fontSize = 40.sp, fontWeight = FontWeight.Bold)
                        Text(text = "꿀잠을 유지하셨어요", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text(text = "이대로만 계속 하세요!!", color = Color.White.copy(alpha = .3f), fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    }
                }
                AverageSleepScore(modifier = modifier)
                SleepSummation(modifier = modifier)
                WeekSleepTime(modifier = modifier)
                WeekLatencyTime(modifier = modifier)
                WeekREMSleepTime(modifier = modifier)
                WeekLightSleepTime(modifier = modifier)
                WeekDeepSleepTime(modifier = modifier)
            }
        }
    }
}