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
import com.example.sleephony.data.model.StatisticData
import com.example.sleephony.data.model.StatisticMySummary
import com.example.sleephony.data.model.StatisticResults
import com.example.sleephony.data.model.StatisticSummaryData
import com.example.sleephony.ui.screen.statistics.components.detail.chart.ComparisonChart
import com.example.sleephony.ui.screen.statistics.components.detail.average.ComparisonAverage
import com.example.sleephony.ui.screen.statistics.week.StatisticsSleepHour
import com.example.sleephony.ui.screen.statistics.week.StatisticsTime

@Composable
fun SleepTimeDetailScreen(
    modifier: Modifier,
    days:List<String>,
    statisticSummary : StatisticSummaryData?,
    statisticComparisonSummary : List<StatisticMySummary?>,
    statistics : StatisticResults?
) {
    val other = if (statisticComparisonSummary[0]?.gender == "M") "${statisticComparisonSummary[0]?.ageGroup} 남성" else "${statisticComparisonSummary[0]?.ageGroup} 여성"
    val mySleepTimeAverage = statisticSummary?.averageSleepTimeMinutes?.toInt() ?: 0
    val otherSleepTimeAverage = statisticComparisonSummary[0]?.sleepDurationMinutes?.toInt() ?: 0
    val averageDifference = if (mySleepTimeAverage> otherSleepTimeAverage) true else false
    val preSleep = statisticSummary?.previousAverageSleepTimeMinutes?.toInt() ?: 0
    val mostSleep = statisticSummary?.mostSleepTimeMinutes?.toInt() ?: 0
    val least = statisticSummary?.leastSleepTimeMinutes?.toInt() ?: 0


    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                ComparisonAverage(
                    modifier = modifier,
                    days = days,
                    sleepHours = statistics?.sleepTime?.map { StatisticsSleepHour(it.value.toInt()) } ?: emptyList(),
                    title = stringResource(R.string.sleep_time),
                    my_name = "내 평균",
                    my_value = mySleepTimeAverage.toFloat(),
                    other_name = "${other} 평균",
                    other_value = otherSleepTimeAverage.toFloat()
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="${other} 평균",
                    before_value= otherSleepTimeAverage.toFloat(),
                    after_name = "내 평균",
                    after_value = mySleepTimeAverage.toFloat(),
                    title = {
                        White_text("${other} 평균 수면시간")
                        Comparison_text(blue_text = "${SummarTime(otherSleepTimeAverage)}", white_text ="보다" )
                        Comparison_text(blue_text = "평균 ${SummarTime(Math.abs(mySleepTimeAverage - otherSleepTimeAverage))}", white_text = if (averageDifference) "더 주무셨어요" else "덜 주무셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="저번주",
                    before_value= preSleep.toFloat(),
                    after_name = "이번주",
                    after_value = mySleepTimeAverage.toFloat(),
                    title = {
                        White_text("지난주보다")
                        Blue_text("${SummarTime(Math.abs(mySleepTimeAverage - preSleep))} 더")
                        White_text(if (mySleepTimeAverage > preSleep)"꿀잠을 유지하셨어요" else "덜 주무셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="최단 수면",
                    before_value= least.toFloat(),
                    after_name = "최장 수면",
                    after_value = mostSleep.toFloat(),
                    title = {
                        White_text("가장 많이 잔날과 적게 잔날은")
                        Comparison_text(blue_text = "${SummarTime(Math.abs(mostSleep - least))}", white_text = "차이나요")
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

fun SummarTime(value: Int): String {
    if (value == 0) return "0분"
    val hour = value / 100
    val min = value % 100
    return if (hour != 0) "${hour}시간 ${min}분" else "${min}분"
    }
