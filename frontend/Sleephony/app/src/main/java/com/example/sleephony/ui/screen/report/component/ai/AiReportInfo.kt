package com.example.sleephony.ui.screen.report.component.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AiReportInfo(
    title: String,
    subtitle: String,
    sleepPeriod: String,
    actualSleep: String,
    fallAsleepTime: String,
    deepSleep: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Black.copy(alpha = .3f),
                shape = RoundedCornerShape(30.dp)
            )
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFF2E3F7C), shape = RoundedCornerShape(28.dp))
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = sleepPeriod,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SleepDetailItem("실제 잔 시간", actualSleep)
                Spacer(modifier = Modifier.height(12.dp))
                SleepDetailItem("잠드는데 걸린 시간", fallAsleepTime)
                Spacer(modifier = Modifier.height(12.dp))
                SleepDetailItem("깊이 잔 시간", deepSleep)
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}


@Composable
fun SleepDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.LightGray, fontSize = 20.sp)
        Text(text = value, color = Color.White, fontSize = 20.sp)
    }
}