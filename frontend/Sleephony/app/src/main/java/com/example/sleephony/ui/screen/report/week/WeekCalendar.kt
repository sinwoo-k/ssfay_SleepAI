package com.example.sleephony.ui.screen.report.week

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.sql.Time
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import androidx.compose.foundation.clickable as clickable1


@Composable
fun WeeklyCalendar(
    modifier: Modifier = Modifier,
    firstDayOfWeek : DayOfWeek,
    weekStartState: LocalDate,
    onPre:(LocalDate) -> Unit,
    onNext:(LocalDate) -> Unit
) {

    val weekStart = weekStartState
    val weekEnd = remember(weekStart) { weekStart.plusDays(6) }

    val state = rememberWeekCalendarState(
        startDate = weekStart,
        endDate = weekEnd,
        firstVisibleWeekDate = weekStart,
        firstDayOfWeek = firstDayOfWeek
    )

    val startMonth = weekStart.monthValue.toString().padStart(2, '0')
    val startDay = weekStart.dayOfMonth.toString().padStart(2, '0')
    val endMonth = weekEnd.monthValue.toString().padStart(2, '0')
    val endDay = weekEnd.dayOfMonth.toString().padStart(2, '0')

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.pre_icon),
            contentDescription = "pre icon",
            modifier = modifier
                .size(25.dp)
                .clickable1 {
                    onPre(weekStart.minusWeeks(1))
                }
        )

        Text(
            text = "${startMonth}.${startDay} ~ ${endMonth}.${endDay}",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 35.sp
        )
        Image(
            painter = painterResource(R.drawable.next_icon),
            contentDescription = "next icon",
            modifier = modifier
                .size(25.dp)
                .clickable1 {
                    onPre(weekStart.plusWeeks(1))
                }
        )
    }
    val calendar_currentMonth = remember { YearMonth.now() }
    val calendar_startMonth = remember { calendar_currentMonth.minusMonths(100) } // Adjust as needed
    val calendar_endMonth = remember { calendar_currentMonth.plusMonths(100) } // Adjust as needed
    val calendar_firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

    val calendar_state = rememberCalendarState(
        startMonth = calendar_startMonth,
        endMonth = calendar_endMonth,
        firstVisibleMonth = calendar_currentMonth,
        firstDayOfWeek = calendar_firstDayOfWeek
    )
    HorizontalCalendar(
        state = calendar_state,
        dayContent = { Day(
            day = it,
            weekStart = weekStart,
            weekEnd = weekEnd,) },
        monthHeader = { month ->
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "${month.yearMonth.year}년 ${month.yearMonth.monthValue}월",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    )
}


@Composable
fun Day(
    day: CalendarDay,
    weekStart:LocalDate,
    weekEnd:LocalDate,
        ) {
    val thisWeek = day.date in weekStart..weekEnd
    Box(
        modifier = Modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = day.date.dayOfMonth.toString(),
                color = Color.White)
            if (thisWeek) {
                Box(
                    modifier = Modifier.size(10.dp)
                        .background(color = colorResource(R.color.SkyBlue), shape = RoundedCornerShape(50.dp))
                )
            }
        }
    }
}