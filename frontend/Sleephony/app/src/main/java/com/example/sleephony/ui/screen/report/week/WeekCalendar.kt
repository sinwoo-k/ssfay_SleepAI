package com.example.sleephony.ui.screen.report.week

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import java.time.LocalDate


@Composable
fun WeeklyCalendar(
    modifier: Modifier = Modifier,
    weekStartState: LocalDate,
    onChange:(LocalDate) -> Unit,
) {

    val weekStart = weekStartState
    val weekEnd = remember(weekStart) { weekStart.plusDays(6) }

    val startMonth = weekStart.monthValue.toString().padStart(2, '0')
    val startDay = weekStart.dayOfMonth.toString().padStart(2, '0')
    val endMonth = weekEnd.monthValue.toString().padStart(2, '0')
    val endDay = weekEnd.dayOfMonth.toString().padStart(2, '0')

    val isOpenWeekAlert = remember { mutableStateOf(false) }

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
                .clickable {
                    onChange(weekStart.minusWeeks(1))
                }
        )

        Text(
            text = "${startMonth}.${startDay} ~ ${endMonth}.${endDay}",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 35.sp,
            modifier = modifier.clickable {
                isOpenWeekAlert.value = true
            }
        )
        Image(
            painter = painterResource(R.drawable.next_icon),
            contentDescription = "next icon",
            modifier = modifier
                .size(25.dp)
                .clickable {
                    onChange(weekStart.plusWeeks(1))
                }
        )
        if (isOpenWeekAlert.value) {
            WeekCalendarAlert(
                weekStart = weekStart,
                weekEnd = weekEnd,
                onRequest = { isOpenWeekAlert.value = false},
                modifier = modifier,
                changRequest = {day -> onChange(day) }
                )
        }
    }
}
