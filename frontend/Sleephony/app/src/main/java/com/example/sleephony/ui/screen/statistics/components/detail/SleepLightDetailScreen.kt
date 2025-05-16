package com.example.sleephony.ui.screen.statistics.components.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sleephony.R
import com.example.sleephony.data.model.StatisticMySummary
import com.example.sleephony.data.model.StatisticResults
import com.example.sleephony.data.model.StatisticSummaryData
import com.example.sleephony.ui.screen.statistics.components.detail.average.ComparisonAverage
import com.example.sleephony.ui.screen.statistics.components.detail.chart.ComparisonChart
import com.example.sleephony.ui.screen.statistics.week.StatisticsSleepHour

@Composable
fun SleepLightDetailScreen(
    modifier:Modifier,
    days:List<String>,
    statisticSummary : StatisticSummaryData?,
    statisticComparisonSummary : List<StatisticMySummary?>,
    statistics : StatisticResults?
) {
    val other = if (statisticComparisonSummary[0]?.gender == "M") "${statisticComparisonSummary[0]?.ageGroup} 남성" else "${statisticComparisonSummary[0]?.ageGroup} 여성"
    val mySleepLightAverage = statisticSummary?.averageSleepTimeMinutes?.toInt() ?: 0
    val mySleepLightRatio = statisticSummary?.averageLightSleepPercentage ?: 0
    val otherSleepLightAverage = statisticComparisonSummary[0]?.lightSleepMinutes ?: 0
    val otherSleepLightRatio = statisticComparisonSummary[0]?.lightSleepRatio ?: 0
    val averageDifference = if (mySleepLightAverage> otherSleepLightAverage) true else false

    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                ComparisonAverage(
                    modifier = modifier,
                    days = days,
                    sleepHours = statistics?.lightSleep?.map { StatisticsSleepHour(it.value.toInt()) } ?: emptyList(),
                    title = stringResource(R.string.light_sleep),
                    my_name = "내 평균",
                    my_value = mySleepLightAverage.toFloat(),
                    other_name = "${other} 평균",
                    other_value = otherSleepLightAverage.toFloat(),
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="${other} 평균",
                    before_value= otherSleepLightAverage.toFloat(),
                    after_name = "내평균",
                    after_value = mySleepLightAverage.toFloat(),
                    title = {
                        White_text("${other} 평균 얕은 수면 시간")
                        Comparison_text(blue_text = "${SummarTime(otherSleepLightAverage)}", white_text = "보다")
                        Comparison_text(blue_text = "평균 ${SummarTime(Math.abs(mySleepLightAverage - otherSleepLightAverage))}", white_text = if (mySleepLightAverage > otherSleepLightAverage) "더 주무셨어요" else "덜 주무셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="${other} 평균",
                    before_value= otherSleepLightRatio.toFloat(),
                    after_name = "내 평균",
                    after_value = mySleepLightRatio.toFloat(),
                    title = {
                        White_text("${other} 평균 얕은 수면 비율")
                        Comparison_text(blue_text = "${otherSleepLightRatio}%" , white_text = "보다")
                        Comparison_text(blue_text = "${Math.abs(mySleepLightRatio - otherSleepLightRatio)}%", white_text = if (mySleepLightRatio > otherSleepLightRatio) "더 많이 주무셨어요" else "더 적게 주무셨어요")
                    }
                )
                Help_comment(
                    modifier = modifier,
                    title = {
                        White_text("얕은 수면이 많으면\n다음과 같은 문제가 생길 수 있어요!!")
                    },
                    value1_image = painterResource(R.drawable.tired_icon),
                    value1_title = "만성 피로",
                    value1_content = "깊은 수면이 부족하면 몸이 제대로 회복되지 않아 아침에 일어나도 피곤함을 느낄 수 있어요.",

                    value2_image = painterResource(R.drawable.brain_icon),
                    value2_title = "집중력 저하",
                    value2_content = "얕은 수면이 많으면 낮 동안 집중하기 어렵고, 일의 효율이 떨어질 수 있어요.",

                    value3_image = painterResource(R.drawable.immune_icon),
                    value3_title = "면역력 약화",
                    value3_content = "깊은 수면은 면역 체계를 강화하는 데 중요한데, 부족하면 쉽게 피로해지고 아플 수 있어요."
                )
            }
        }
    }
}