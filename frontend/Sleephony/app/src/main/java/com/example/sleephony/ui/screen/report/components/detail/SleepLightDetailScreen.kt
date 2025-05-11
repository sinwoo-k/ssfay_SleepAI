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
fun SleepLightDetailScreen(
    modifier:Modifier,
    days:List<String>
) {
    val sleepHours = remember { listOf(490f, 410f,320f, 340f, 310f, 390f, 380f) }
    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                ComparisonAverage(
                    modifier = modifier,
                    days = days,
                    sleepHours = sleepHours,
                    title = stringResource(R.string.REM_sleep),
                    my_name = "내 평균",
                    my_value = 430f,
                    other_name = "20대 남성 평균",
                    other_value = 410f
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="남성 평균",
                    before_value= 410f,
                    after_name = "내평균",
                    after_value = 430f,
                    title = {
                        White_text("20대 남서 평균 얕은 수면 시간")
                        Comparison_text(blue_text = "4시간 10분", white_text = "보다")
                        Comparison_text(blue_text = "평균 20분", white_text = "더 주무셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="남성 평균",
                    before_value= 55f,
                    after_name = "내 평균",
                    after_value = 62f,
                    title = {
                        White_text("20대 남서 평균 얕은 수면 비율")
                        Comparison_text(blue_text = "55%" , white_text = "보다")
                        Comparison_text(blue_text = "7%", white_text = "더 많이 주무셨어요")
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