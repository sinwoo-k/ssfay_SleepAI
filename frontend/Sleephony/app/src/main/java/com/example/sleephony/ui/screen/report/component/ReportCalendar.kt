package com.example.sleephony.ui.screen.report.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.sleephony.ui.screen.report.viewmodel.ReportViewModel
import kotlinx.coroutines.launch

@Composable
fun ReportCalendar(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ReportViewModel,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var showMonthDialog by remember { mutableStateOf(false) }

    val year = selectedDate.year
    val month = selectedDate.monthValue
    val yearMonth = YearMonth.of(year, month)
    val lastDay = yearMonth.lengthOfMonth()

    val monthStr = "%04d-%02d".format(year, month)
    LaunchedEffect(monthStr) {
        viewModel.getReportDates(monthStr)
    }

    val reportDateList by viewModel.reportDateList.collectAsState()
    val dotDates = remember(reportDateList) {
        reportDateList.mapNotNull { runCatching { LocalDate.parse(it) }.getOrNull() }.toSet()
    }

    val dates = (1..lastDay).map { day ->
        val date = LocalDate.of(year, month, day)
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
        Triple(day, dayOfWeek, date)
    }

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val selectedIndex = dates.indexOfFirst { it.third == selectedDate }

    LaunchedEffect(selectedIndex) {
        if (selectedIndex != -1) {
            coroutineScope.launch {
                scrollState.animateScrollToItem(
                    index = maxOf(0, selectedIndex - 2),
                    scrollOffset = -80
                )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Text(
                text = "${year}년 ${month}월",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        showMonthDialog = true
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.ic_ai),
                contentDescription = "AI Icon",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .size(44.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        navController.navigate("ai_report/${selectedDate}")
                    },
                contentScale = ContentScale.Fit
            )
        }

        LazyRow(
            state = scrollState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 40.dp)
        ) {
            items(dates) { (day, dayOfWeek, date) ->
                val isSelected = selectedDate == date
                val hasData = dotDates.contains(date)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(40.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onDateSelected(date)  // 날짜 선택 시 부모의 상태 업데이트
                        }
                ) {
                    Text(
                        text = dayOfWeek.first().toString(),
                        color = if (isSelected) Color.White else Color.LightGray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = if (isSelected) Color.White else Color.Transparent,
                                shape = CircleShape
                            )
                    ) {
                        Text(
                            text = day.toString(),
                            color = if (isSelected) Color.Black else Color.LightGray,
                            fontSize = 18.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (hasData) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF5A88FF), CircleShape)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    if (showMonthDialog) {
        MonthlyCalendarAlert(
            monthState = selectedDate,
            modifier = Modifier,
            onRequest = { showMonthDialog = false },
            changRequest = {
                onDateSelected(it)  // 날짜 변경 시 상태 업데이트
                showMonthDialog = false
            }
        )
    }
}
