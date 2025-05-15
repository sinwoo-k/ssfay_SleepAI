package com.example.sleephony.ui.screen.report.component.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    selected: SleepCategory = SleepCategory.TOTAL,
    previousSleep: String = "1시간",
    currentSleep: String = "1시간 29분",
    diffText: String = "29분 더 많이 잤습니다",
    modifier: Modifier = Modifier
) {
    val selectedCategory = remember { mutableStateOf(selected) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Black.copy(alpha = .3f),
                shape = RoundedCornerShape(30.dp)
            )
            .padding(20.dp)
    ) {

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

        Text(
            text = buildAnnotatedString {
                append("평균보다\n")
                withStyle(style = SpanStyle(color = colorResource(R.color.SkyBlue), fontSize = 40.sp)) {
                    append(diffText.split(" ")[0])
                }
                append(" 더 많이 잤습니다")
            },
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 50.sp
        )


        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // 지난 시간
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = previousSleep, color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(120.dp)
                                .background(Color(0xFF6A8BC5), shape = RoundedCornerShape(8.dp))
                        )
                    }

                    // 이번 시간
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = currentSleep, color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(150.dp)
                                .background(Color(0xFF80B6FF), shape = RoundedCornerShape(8.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.3f))
                )
            }
        }
    }
}
