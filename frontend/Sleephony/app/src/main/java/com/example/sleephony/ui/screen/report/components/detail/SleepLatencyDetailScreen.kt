package com.example.sleephony.ui.screen.report.components.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.report.components.detail.chart.ComparisonChart
import com.example.sleephony.ui.screen.report.components.detail.average.ComparisonAverage

@Composable
fun SleepLatencyDetailScreen(
    modifier: Modifier,
    days:List<String>
) {
    val sleepHours = remember { listOf(4f, 11f, 4f, 22f, 8f, 5f, 7f) }
    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                ComparisonAverage(
                    modifier = modifier,
                    days = days,
                    sleepHours = sleepHours,
                    title = stringResource(R.string.non_sleep),
                    my_name = "내 평균",
                    my_value = 14f,
                    other_name = "20대 남성 평균",
                    other_value = 20f
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="저번주",
                    before_value= 20f,
                    after_name = "이번주",
                    after_value = 14f,
                    title = {
                        White_text("20대 남서 평균 잠복기인")
                        Comparison_text(blue_text = "20분", white_text = "범위 안에")
                        White_text("잘 주무셨네요!!")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="남성 평균",
                    before_value= 1100f,
                    after_name = "내 평균",
                    after_value = 1158f,
                    title = {
                        White_text("20대 남서 평균 취침 시간")
                        Comparison_text(blue_text = "11분" , white_text = "보다")
                        Comparison_text(blue_text = "평균 58분", white_text = "더 늦게 주무셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="남성 평균",
                    before_value= 700f,
                    after_name = "내 평균",
                    after_value = 703f,
                    title = {
                        White_text("20대 남서 평균 기상 시간")
                        Comparison_text(blue_text = "7시", white_text = "보다")
                        Comparison_text(blue_text = "평균 3분", white_text = "더 주무셨어요" )
                    }
                )
            }
        }
    }
}
