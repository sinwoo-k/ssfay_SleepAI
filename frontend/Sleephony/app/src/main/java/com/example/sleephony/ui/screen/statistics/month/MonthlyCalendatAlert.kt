package com.example.sleephony.ui.screen.statistics.month

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.statistics.components.detail.White_text
import com.kizitonwose.calendar.compose.HorizontalYearCalendar
import com.kizitonwose.calendar.compose.yearcalendar.rememberYearCalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth


@OptIn(com.kizitonwose.calendar.core.ExperimentalCalendarApi::class)
@Composable
fun MonthlyCalendarAlert(
    monthState:LocalDate,
    modifier: Modifier,
    onRequest:()-> Unit,
    changRequest:(LocalDate) -> Unit
) {
    val calendar_currentYear = remember { Year.now() }
    val calendar_startYear = remember { calendar_currentYear.minusYears(100) }
    val calendar_endYear = remember { calendar_currentYear.plusYears(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val calendar_state = rememberYearCalendarState(
    startYear = calendar_startYear,
    endYear = calendar_endYear,
    firstVisibleYear = calendar_currentYear,
    firstDayOfWeek = firstDayOfWeek
    )
    AlertDialog(
        containerColor = colorResource(R.color.dark_navy),
        title = {
            val data = calendar_state.firstVisibleYear
            White_text("${data.year}년")
                },
        text = {
            HorizontalYearCalendar(
                state = calendar_state,
                monthHeader = {month->
                    MonthHeader(
                        month = month,
                        current = monthState,
                        changRequest = {
                            changRequest(month.yearMonth.atDay(1))
                        }
                    )
                },
                dayContent = {}
            )
        },
        confirmButton = {  },
        onDismissRequest = { onRequest() }
    )
}

@Composable
fun MonthHeader(
    month: CalendarMonth,
    current:LocalDate,
    changRequest: () -> Unit
) {
    val isSelected = YearMonth.from(current) == month.yearMonth
    Column(
        modifier = Modifier
            .clickable {
                changRequest()
            }
            .aspectRatio(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${month.yearMonth.monthValue}월",
            modifier = Modifier,
            color = if (isSelected) colorResource(R.color.SkyBlue) else  Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}
