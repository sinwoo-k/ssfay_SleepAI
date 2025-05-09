package com.example.sleephony.ui.screen.report.components.detail.sleepcycle.sleep_latency

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.report.components.detail.chart.ComparisonBarChart

@Composable
fun ComparisonSleepLatency(
    modifier: Modifier,
    days:List<String>,
    sleepHours:List<Float>
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(modifier = modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.non_sleep),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = modifier
                    .padding(3.dp, 0.dp)
                    .background(
                        color = Color.Black.copy(alpha = .3f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                Column {
                    ComparisonBarChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(top = 15.dp),
                        days = days, sleepHours = sleepHours,
                        before = 20f, after = 14f
                    )
                    Column(
                        modifier = modifier.fillMaxWidth().padding(15.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                Text(
                                    text = "내 평균",
                                    color = colorResource(R.color.light_gray),
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "7시간 3분",
                                color = Color.White.copy(alpha = .3f),
                                fontSize = 20.sp
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
                                            color = colorResource(R.color.steel_blue),
                                            shape = RoundedCornerShape(50.dp)
                                        )
                                )
                                Text(
                                    text = "남성 평균",
                                    color = colorResource(R.color.steel_blue),
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "6시간 53분",
                                color = Color.White.copy(alpha = .3f),
                                fontSize = 20.sp
                            )
                        }
                    }
                }

            }
        }
    }
}