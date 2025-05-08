package com.example.sleephony.ui.screen.report.components.detail.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.sleephony.R

@Composable
fun ComparisonAverage(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .padding(3.dp)
            .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .height(350.dp)
    ) {
        Column {
            val days = remember { listOf("월", "화", "수", "목", "금", "토", "일") }
            val sleepHours = remember { listOf(7.5f, 6.8f, 8.2f, 7.0f, 6.5f, 8.5f, 9.0f) }
            ComparisonBarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(top = 15.dp),
                days = days, sleepHours = sleepHours
            )
            Column (
                modifier = modifier.fillMaxWidth().padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(
                                    color = colorResource(R.color.light_gray),
                                    shape = RoundedCornerShape(50.dp)
                                )
                        )
                        Text(text = "내 평균", color = colorResource(R.color.light_gray))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "7시간 3분",
                        color = Color.White.copy(alpha = .3f)
                    )
                }
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(
                                    color = colorResource(R.color.dark_navy),
                                    shape = RoundedCornerShape(50.dp)
                                )
                        )
                        Text(text = "내 평균", color = colorResource(R.color.dark_navy))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "6시간 53분",
                        color = Color.White.copy(alpha = .3f)
                    )
                }
            }
        }

    }
}