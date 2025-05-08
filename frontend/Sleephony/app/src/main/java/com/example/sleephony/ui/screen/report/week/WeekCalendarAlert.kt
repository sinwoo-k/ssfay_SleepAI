package com.example.sleephony.ui.screen.report.week

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

@Composable
fun WeekCalendarAlert(
    modifier: Modifier,
    weekStart:LocalDate,
    weekEnd:LocalDate,
    onRequest:()-> Unit,
    changRequest:(LocalDate) -> Unit
) {
    val calendar_currentMonth = remember { YearMonth.now() }
    val calendar_startMonth = remember { calendar_currentMonth.minusMonths(100) }
    val calendar_endMonth = remember { calendar_currentMonth.plusMonths(100) }
    val calendar_firstDayOfWeek = remember { firstDayOfWeekFromLocale() }


    val calendar_state = rememberCalendarState(
        startMonth = calendar_startMonth,
        endMonth = calendar_endMonth,
        firstVisibleMonth = calendar_currentMonth,
        firstDayOfWeek = calendar_firstDayOfWeek
    )
    AlertDialog(
        containerColor = colorResource(R.color.dark_navy),
        title = {
            val data = calendar_state.firstVisibleMonth
            Text(text = "${data.yearMonth.year}년 ${data.yearMonth.monthValue}월",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )},
        text = {
            HorizontalCalendar(
                state = calendar_state,
                dayContent = {
                    val data = calendar_state.firstVisibleMonth
                    Day(
                        day = it,
                        weekStart = weekStart,
                        weekEnd = weekEnd,
                        currentMonth = data.yearMonth,
                        modifier = modifier,
                        changeDay = { day -> changRequest(day) }
                    )
                             },

                monthHeader = { month ->
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val days = listOf("월","화","수","목","금","토","일")
                        days.forEach {day ->
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = day, color = Color.White )
                            }
                        }
                    }
                }
            )
        },
        confirmButton = {  },
        onDismissRequest = { onRequest() }
    )
}

@Composable
fun Day(
    modifier: Modifier,
    day: CalendarDay,
    weekStart:LocalDate,
    weekEnd:LocalDate,
    currentMonth: YearMonth,
    changeDay:(LocalDate) -> Unit
) {
    val thisWeek = day.date in weekStart..weekEnd
    val thisMonth = currentMonth.month == day.date.month
    Box(
        modifier = modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                modifier = modifier.clickable {
                    val clickDay = day.date
                    val dayOfWeek = clickDay.dayOfWeek.value
                    val startOfWeek = clickDay.plusDays((7-dayOfWeek).toLong()).minusWeeks(1)
                    changeDay(startOfWeek)
                },
                color = when(thisMonth) {
                    true -> Color.White
                    false -> Color.White.copy(alpha = .3f)
                }
                        )
                        if (thisWeek) {
                            Box(
                                modifier = modifier
                                    .size(10.dp)
                                    .background(
                                        color = colorResource(R.color.SkyBlue),
                                        shape = RoundedCornerShape(50.dp)
                                    )
                            )
                        }
                }
        }
    }
