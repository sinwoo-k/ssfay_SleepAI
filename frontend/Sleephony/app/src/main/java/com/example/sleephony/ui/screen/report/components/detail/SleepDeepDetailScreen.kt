package com.example.sleephony.ui.screen.report.components.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.report.components.detail.average.ComparisonAverage
import com.example.sleephony.ui.screen.report.components.detail.chart.ComparisonChart

@Composable
fun SleepDeepDetailScreen(
    modifier: Modifier,
    days:List<String>
) {
    val sleepHours = remember { listOf(130f, 100f,110f, 120f, 120f, 150f, 110f) }
    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                ComparisonAverage(
                    modifier = modifier,
                    days = days,
                    sleepHours = sleepHours,
                    title = stringResource(R.string.deep_sleep),
                    my_name = "내 평균",
                    my_value = 113f,
                    other_name = "20대 남성 평균",
                    other_value = 102f
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="남성 평균",
                    before_value= 102f,
                    after_name = "내평균",
                    after_value = 113f,
                    title = {
                        White_text("20대 남서 평균 깊은 수면 시간")
                        Comparison_text(blue_text = "1시간 2분", white_text = "보다")
                        Comparison_text(blue_text = "평균 11분", white_text = "더 주무셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="남성 평균",
                    before_value= 15f,
                    after_name = "내 평균",
                    after_value = 13f,
                    title = {
                        White_text("20대 남서 평균 렘 수면 비율")
                        Comparison_text(blue_text = "15%" , white_text = "보다")
                        Comparison_text(blue_text = "2%", white_text = "더 주무셨어요")
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