package com.example.sleephony.ui.screen.report.component.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.report.viewmodel.SleepCategory

@Composable
fun SleepComparisonCard(
    modifier: Modifier = Modifier,
    totalSleep: Int,
    avgTotalSleep: Int,
    deepSleep: Int,
    avgDeepSleep: Int,
    remSleep: Int,
    avgRemSleep: Int,
) {
    val selectedCategory = remember { mutableStateOf(SleepCategory.TOTAL) }

    val (currentMinutes, previousMinutes) = when (selectedCategory.value) {
        SleepCategory.TOTAL -> totalSleep to avgTotalSleep
        SleepCategory.DEEP -> deepSleep to avgDeepSleep
        SleepCategory.REM -> remSleep to avgRemSleep
    }

    val currentSleepText = "${currentMinutes / 60}시간 ${currentMinutes % 60}분"
    val previousSleepText = "${previousMinutes / 60}시간 ${previousMinutes % 60}분"
    val diff = currentMinutes - previousMinutes
    val diffText = when {
        diff > 0 -> "${diff / 60}시간 ${diff % 60}분 더 많이 잤습니다"
        diff < 0 -> "${-diff / 60}시간 ${-diff % 60}분 덜 잤습니다"
        else -> "같이 잤습니다"
    }

    val maxMinutes = maxOf(currentMinutes, previousMinutes).coerceAtLeast(1)
    val currHeightRatio = currentMinutes / maxMinutes.toFloat()
    val prevHeightRatio = previousMinutes / maxMinutes.toFloat()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Black.copy(alpha = .3f),
                shape = RoundedCornerShape(30.dp)
            )
            .padding(20.dp)
    ) {
        // 카테고리 선택 탭
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SleepCategory.values().forEach { category ->
                val isSelected = selectedCategory.value == category
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isSelected) Color(0xFF3A4D77) else Color(0xFF2A3A5E),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable { selectedCategory.value = category }
                ) {
                    Text(
                        text = category.label,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 수면 차이 텍스트
        Text(
            text = buildAnnotatedString {
                append("평균보다\n")
                withStyle(
                    style = SpanStyle(
                        color = colorResource(R.color.SkyBlue),
                        fontSize = 40.sp
                    )
                ) {
                    append(diffText.split(" ")[0])
                }
                append(" " + diffText.split(" ").drop(1).joinToString(" "))
            },
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 50.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 막대 그래프
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            // 평균 수면
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = previousSleepText, color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height((150 * prevHeightRatio).dp)
                        .background(Color(0xFF6A8BC5), shape = RoundedCornerShape(8.dp))
                )
            }

            // 오늘 수면
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = currentSleepText, color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height((150 * currHeightRatio).dp)
                        .background(Color(0xFF80B6FF), shape = RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 전체 선
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.3f))
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 텍스트 라벨
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "평균", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            Text(text = "오늘", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}
