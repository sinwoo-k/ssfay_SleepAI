package com.example.sleephony.ui.screen.report.week

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sleephony.R

@Composable
fun WeekLatencyTime(
    modifier: Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = stringResource(R.string.sleep_latency_time), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 30.sp)
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.SkyBlue))) {
                append("1시간 13분")
            }
            withStyle(style = SpanStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)) {
                append("이에요")
            }
        })
        Box(modifier = modifier
            .padding(3.dp)
            .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .height(200.dp)
            .clickable {
                navController.navigate("detail/sleep_latency")
            }
        ) {
            val days = remember { listOf("월", "화", "수", "목", "금", "토", "일") }
            val sleepHours = remember { listOf(5f, 14f, 8f, 21f, 11f, 6f, 8f) }
            Row(
                modifier = modifier.fillMaxWidth()
                    .padding(end = 15
                        .dp),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(R.drawable.more_icon),
                    contentDescription = "더보기 아이콘",
                    modifier = modifier.size(30.dp),
                )
            }
            WeekChart(modifier = Modifier.fillMaxSize().padding(top=15.dp), days = days, sleepHours = sleepHours)
        }
    }
}