package com.example.sleephony.ui.screen.statistics.components.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.statistics.components.detail.chart.ComparisonChart
import com.example.sleephony.ui.screen.statistics.components.detail.average.ComparisonAverage

@Composable
fun SleepTimeDetailScreen(
    modifier: Modifier,
    days:List<String>,
) {
    val sleepHours = remember { listOf(7.5f, 6.8f, 8.2f, 7.0f, 6.5f, 8.5f, 9.0f) }
    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                ComparisonAverage(
                    modifier = modifier,
                    days = days,
                    sleepHours = sleepHours,
                    title = stringResource(R.string.sleep_time),
                    my_name = "내 평균",
                    my_value = 703f,
                    other_name = "20대 남성 평균",
                    other_value = 658f,
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="남성 평균",
                    before_value= 657f,
                    after_name = "내 평균",
                    after_value = 703f,
                    title = {
                        White_text("20대 남성 평균 수명시간")
                        Comparison_text(blue_text = "6시간 57분", white_text ="보다" )
                        Comparison_text(blue_text = "평균 6분", white_text = "더 주무셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="저번주",
                    before_value= 648f,
                    after_name = "이번주",
                    after_value = 703f,
                    title = {
                        White_text("지난주보다")
                        Blue_text("15분 더")
                        White_text("꿀잠을 유지하셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="최단 수면",
                    before_value= 603f,
                    after_name = "최장 수면",
                    after_value = 803f,
                    title = {
                        White_text("가장 많이 잔날과 적게 잔날은")
                        Comparison_text(blue_text = "2시간", white_text = "차이나요")
                    }
                )
            }
        }
    }
}

@Composable
fun White_text(
    text:String
) {
    Text(
        text = text,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        lineHeight = 32.sp
    )
}

@Composable
fun Blue_text(
    text: String
) {
    Text(
        text = "$text",
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.SkyBlue)
    )
}

@Composable
fun Gray_text(
    text: String
) {
    Text(
        text = "$text",
        color = Color.White.copy(alpha = .3f),
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun Comparison_text(
    blue_text:String,
    white_text:String
) {
    Text(text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 40.sp, fontWeight = FontWeight.Bold, color = colorResource(
                    R.color.SkyBlue
                )
            )
        ) {
            append(blue_text)
        }
        append(" ")
        withStyle(
            style = SpanStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        ) {
            append(white_text)
        }
    })
}

