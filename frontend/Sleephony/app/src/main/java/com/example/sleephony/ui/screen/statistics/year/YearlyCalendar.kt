package com.example.sleephony.ui.screen.statistics.year

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
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun YearlyCalendar(
    modifier: Modifier = Modifier,
    yearState: LocalDate,
    onChange:(LocalDate) -> Unit,
) {

    val isOpenWeekAlert = remember { mutableStateOf(false) }
    val year = yearState.format(DateTimeFormatter.ofPattern("yyyy년").withLocale(Locale.KOREAN))

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
                    onChange(yearState.minusYears(1))
                }
        )

        Text(
            text = "${year}",
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
                    onChange(yearState.plusYears(1))
                }
        )
        if (isOpenWeekAlert.value) {
            YearlyCalendarAlert(
                onRequest = { isOpenWeekAlert.value = false },
                modifier = modifier,
                changRequest = { day -> onChange(day) },
                yearState = yearState
            )
        }
    }
}