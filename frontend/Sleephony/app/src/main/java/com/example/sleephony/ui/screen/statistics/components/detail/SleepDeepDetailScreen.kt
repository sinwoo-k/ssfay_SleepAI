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
fun SleepDeepDetailScreen(
    modifier: Modifier,
    days:List<String>,
    statisticSummary : StatisticSummaryData?,
    statisticComparisonSummary : List<StatisticMySummary?>,
    statistics : StatisticResults?
) {

    val other = if (statisticComparisonSummary[0]?.gender == "M") "${statisticComparisonSummary[0]?.ageGroup} 남성" else "${statisticComparisonSummary[0]?.ageGroup} 여성"
    val mySleepDeepAverage = statisticSummary?.averageDeepSleepMinutes?.toInt() ?: 0
    val mySleepDeepRatio = statisticSummary?.averageDeepSleepPercentage ?: 0
    val otherSleepDeepAverage = statisticComparisonSummary[0]?.deepSleepMinutes ?: 0
    val otherSleepDeepRatio = statisticComparisonSummary[0]?.deepSleepRatio ?: 0

    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                ComparisonAverage(
                    modifier = modifier,
                    days = days,
                    sleepHours = statistics?.deepSleep?.map { StatisticsSleepHour(it.value.toInt()) } ?: emptyList(),
                    title = stringResource(R.string.deep_sleep),
                    my_name = "내 평균",
                    my_value = mySleepDeepAverage.toFloat(),
                    other_name = "${other} 평균",
                    other_value = otherSleepDeepAverage.toFloat(),
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="${other} 평균",
                    before_value= otherSleepDeepAverage.toFloat(),
                    after_name = "내평균",
                    after_value = mySleepDeepAverage.toFloat(),
                    title = {
                        White_text("${other} 평균 깊은 수면 시간")
                        Comparison_text(blue_text = "${SummarTime(otherSleepDeepAverage)}", white_text = "보다")
                        Comparison_text(blue_text = "평균 ${Math.abs(mySleepDeepAverage - otherSleepDeepAverage)}분", white_text =if (mySleepDeepAverage > otherSleepDeepAverage ) "더 주무셨어요" else "적게 주무셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="${other} 평균",
                    before_value= otherSleepDeepRatio.toFloat(),
                    after_name = "내 평균",
                    after_value = mySleepDeepRatio.toFloat(),
                    title = {
                        White_text("${other} 평균 렘 수면 비율")
                        Comparison_text(blue_text = "${otherSleepDeepRatio}%" , white_text = "보다")
                        Comparison_text(blue_text = "${Math.abs(mySleepDeepRatio - otherSleepDeepRatio)}%", white_text = if (mySleepDeepRatio > otherSleepDeepRatio ) "더 주무셨어요" else "적게 주무셨어요")
                    }
                )
                Help_comment(
                    modifier = modifier,
                    title= {
                        White_text("깊은 수면을 위해, \n" +
                                "작은 습관부터 시작해봐요")
                    },
                    value1_image = painterResource(R.drawable.phone_icon),
                    value1_title = "자기전 1시간 전에 스마트폰 끄기",
                    value1_content = "스마트폰에서 나오는 블루라이트는 수면에 방해가 될 수 있어요",
                    value2_image = painterResource(R.drawable.relax_icon),
                    value2_title = "취침 직전 스트레칭하기",
                    value2_content= "가벼운 스트레칭은 몸을 릴랙스시키고, 잠드는 데 도움이 돼요",
                    value3_image= painterResource(R.drawable.bed_icon),
                    value3_title= "일정한 수면 루틴 유지하기",
                    value3_content = "매일 같은 시간에 잠자리에 드는 습관은 몸의 생체 리듬을 맞춰 더 쉽게 수면을 유도할 수 있어요"
                )
            }
        }
    }
}