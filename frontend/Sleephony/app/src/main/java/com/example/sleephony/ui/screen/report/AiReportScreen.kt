package com.example.sleephony.ui.screen.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sleephony.R
import com.example.sleephony.ui.screen.report.component.ai.AiReportInfo
import com.example.sleephony.ui.screen.report.component.ai.AiReportPrompt
import com.example.sleephony.ui.screen.report.component.ai.ScoreGauge
import com.example.sleephony.ui.screen.report.component.ai.SleepComparisonCard

@Composable
fun AiReportScreen(navController: NavController) {
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

            // 날짜
            Text(
                text = "4.23 (화)",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 점수
            Text(
                text = "95점 / 100점",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(36.dp))

            ScoreGauge(score = 95, maxScore = 100, width = 260.dp, height = 100.dp)

            Image(
                painter = painterResource(id = R.drawable.ic_sleepy_cow),
                contentDescription = "캐릭터",
                modifier = Modifier
                    .size(270.dp)
                    .offset(y = (-80).dp)
            )

            // 수면 정보
            AiReportInfo(
                title = "스트레스 해소의 잠",
                subtitle = "램수면 비율이 높았어요!\n창의력을 발휘하도록 돕는 잠이랍니다",
                sleepPeriod = "오후 11:40 ~ 오전 09:03",
                actualSleep = "5시간 50분",
                fallAsleepTime = "5분",
                deepSleep = "2시간 22분",
                modifier = Modifier.offset(y = (-60).dp)
            )

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

            AiReportPrompt(modifier = Modifier.offset(y = (-40).dp))

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

            SleepComparisonCard(modifier = Modifier.offset(y = (-40).dp))

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}