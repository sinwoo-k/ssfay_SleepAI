package com.example.sleephony.ui.screen.report.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.statistics.month.MonthlyCalendarAlert
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import androidx.navigation.NavController

@Composable
fun ReportCalendar(
    modifier: Modifier = Modifier,
    initialDate: LocalDate = LocalDate.now(),
    navController: NavController
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var showMonthDialog by remember { mutableStateOf(false) }

    val year = selectedDate.year
    val month = selectedDate.monthValue
    val yearMonth = YearMonth.of(year, month)
    val lastDay = yearMonth.lengthOfMonth()
    val dotDays = setOf(3, 6, 9, 12, 18, 25)

    val dates = (1..lastDay).map { day ->
        val date = LocalDate.of(year, month, day)
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
        Triple(day, dayOfWeek, date)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // ✅ 상단 헤더: 가운데 텍스트, 오른쪽 AI 아이콘
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            // 가운데 텍스트
            Text(
                text = "${year}년 ${month}월",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable { showMonthDialog = true }
            )

            // 오른쪽 AI 아이콘
            Image(
                painter = painterResource(id = R.drawable.ic_ai),
                contentDescription = "AI Icon",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .size(44.dp)
                    .clickable {
                        navController.navigate("ai_report")
                    },
                contentScale = ContentScale.Fit
            )
        }

        // ✅ 날짜 리스트
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dates) { (day, dayOfWeek, _) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(48.dp)
                ) {
                    Text(
                        text = dayOfWeek.first().toString(),
                        color = Color.LightGray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = day.toString(),
                        color = if (day == selectedDate.dayOfMonth) Color.White else Color.LightGray,
                        fontSize = 18.sp,
                        fontWeight = if (day == selectedDate.dayOfMonth) FontWeight.Bold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    if (dotDays.contains(day)) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color(0xFF5A88FF), CircleShape)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    // ✅ 월 선택 다이얼로그
    if (showMonthDialog) {
        MonthlyCalendarAlert(
            monthState = selectedDate,
            modifier = Modifier,
            onRequest = { showMonthDialog = false },
            changRequest = {
                selectedDate = it
                showMonthDialog = false
            }
        )
    }
}
