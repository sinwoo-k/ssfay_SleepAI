package com.example.sleephony.ui.screen.report.components.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.sleephony.ui.screen.report.components.detail.chart.ComparisonAverage
import com.example.sleephony.ui.screen.report.components.detail.chart.ComparisonBarChart
import com.example.sleephony.ui.screen.report.components.detail.chart.ComparisonChart

@Composable
fun SleepTimeDetailScreen(
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp)
    ) {
        item {
            ComparisonAverage(modifier = modifier)
            ComparisonSleep(modifier = modifier)
            ComparisonBefore(modifier = modifier)
            ComparisonMost(modifier = modifier)
        }
    }
}

@Composable
fun  ComparisonSleep(
    modifier: Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "20대 남성 평균 수명시간",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 40.sp, fontWeight = FontWeight.Bold, color = colorResource(
                            R.color.SkyBlue
                        )
                    )
                ) {
                    append("6시간 57분")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                ) {
                    append(" 보다")
                }
            })
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 40.sp, fontWeight = FontWeight.Bold, color = colorResource(
                            R.color.SkyBlue
                        )
                    )
                ) {
                    append("평균 6분")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                ) {
                    append(" 더 주무셨어요")
                }
            })
            ComparisonChart(
                modifier = modifier,
                before_name="남성 평균",
                before_value= 657f,
                after_name = "내 평균",
                after_value = 703f
                )
        }
    }
}

@Composable
fun ComparisonBefore(
    modifier: Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "지난주보다",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(text = "15분 더", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.SkyBlue))
            Text(text ="꿀잠을 유지하셨어요", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White )
            ComparisonChart(
                modifier = modifier,
                before_name="저번주",
                before_value= 648f,
                after_name = "이번주",
                after_value = 703f
            )
        }
    }
}

@Composable
fun  ComparisonMost(
    modifier: Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "가장 많이 잔날과 적게 잔날의",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                ) {
                    append("차이는")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 40.sp, fontWeight = FontWeight.Bold, color = colorResource(
                            R.color.SkyBlue
                        )
                    )
                ) {
                    append(" 2시간 ")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                ) {
                    append("이예요")
                }
            })
            ComparisonChart(
                modifier = modifier,
                before_name="최단 수면",
                before_value= 603f,
                after_name = "최장 수면",
                after_value = 803f
            )
        }
    }
}

