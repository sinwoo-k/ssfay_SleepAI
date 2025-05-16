package com.example.sleephony.ui.screen.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sleephony.R
import com.example.sleephony.ui.screen.report.component.ai.AiReportInfo
import com.example.sleephony.ui.screen.report.component.ai.AiReportPrompt
import com.example.sleephony.ui.screen.report.component.ai.ScoreGauge
import com.example.sleephony.ui.screen.report.component.ai.SleepComparisonCard
import com.example.sleephony.ui.screen.report.viewmodel.ReportViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AiReportScreen(
    navController: NavController,
    selectedDate: LocalDate
) {
    val viewModel: ReportViewModel = hiltViewModel()
    val aiDetail by viewModel.aiReport.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getReportDetailed(selectedDate.toString())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF182741),
                        Color(0xFF314282),
                        Color(0xFF1D1437)
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(R.drawable.bg_stars),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.FillWidth
        )

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.popBackStack() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "← 뒤로가기",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("M.d (E)", Locale.KOREAN)),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (aiDetail == null) {
                Text(
                    text = "아직 수면 기록이 없어요.\n다른 날짜를 선택해 주세요.",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 80.dp),
                    lineHeight = 30.sp
                )
                return@Column
            }

            Text(
                text = "${aiDetail?.sleepScore ?: 0}점 / 100점",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(36.dp))

            ScoreGauge(score = aiDetail?.sleepScore ?: 0, maxScore = 100, width = 260.dp, height = 100.dp)

            Image(
                painter = painterResource(id = R.drawable.ic_sleepy_cow),
                contentDescription = "캐릭터",
                modifier = Modifier
                    .size(270.dp)
                    .offset(y = (-80).dp)
            )

            aiDetail?.let {
                val formatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN)
                val sleepStartFormatted = LocalDateTime.parse(it.sleepStart).format(formatter)
                val sleepEndFormatted = LocalDateTime.parse(it.sleepEnd).format(formatter)
                val sleepPeriod = "$sleepStartFormatted ~ $sleepEndFormatted"
                AiReportInfo(
                    title = it.title,
                    subtitle = it.description,
                    sleepPeriod = sleepPeriod,
                    actualSleep = "${it.totalSleepMinutes / 60}시간 ${it.totalSleepMinutes % 60}분",
                    fallAsleepTime = "${it.sleepLatencyMinutes}분",
                    deepSleep = "${it.deepSleepMinutes / 60}시간 ${it.deepSleepMinutes % 60}분",
                    modifier = Modifier.offset(y = (-60).dp)
                )
            }

            Spacer(modifier = Modifier.height(0.dp))

            Text(
                text = "수면 분석",
                fontSize = 30.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .offset(y = (-40).dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            AiReportPrompt(
                modifier = Modifier.offset(y = (-40).dp),
                selectedDate = selectedDate
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "수면 비교",
                fontSize = 30.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .offset(y = (-40).dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            aiDetail?.let {
                SleepComparisonCard(
                    modifier = Modifier.offset(y = (-40).dp),
                    totalSleep = it.totalSleepMinutes,
                    avgTotalSleep = it.statistics.avgTotalSleepMinutes,
                    deepSleep = it.deepSleepMinutes,
                    avgDeepSleep = it.statistics.avgDeepSleepMinutes,
                    remSleep = it.remSleepMinutes,
                    avgRemSleep = it.statistics.avgRemSleepMinutes,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
