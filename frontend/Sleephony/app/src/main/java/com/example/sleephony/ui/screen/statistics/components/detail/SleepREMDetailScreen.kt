package com.example.sleephony.ui.screen.statistics.components.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.data.model.StatisticMySummary
import com.example.sleephony.data.model.StatisticResults
import com.example.sleephony.data.model.StatisticSummaryData
import com.example.sleephony.ui.screen.statistics.components.detail.average.ComparisonAverage
import com.example.sleephony.ui.screen.statistics.components.detail.chart.ComparisonChart
import com.example.sleephony.ui.screen.statistics.week.StatisticsSleepHour

@Composable
fun SleepREMDetailScreen(
    modifier: Modifier,
    days:List<String>,
    statisticSummary : StatisticSummaryData?,
    statisticComparisonSummary : List<StatisticMySummary?>,
    statistics : StatisticResults?
) {
    val other = if (statisticComparisonSummary[0]?.gender == "M") "${statisticComparisonSummary[0]?.ageGroup} 남성" else "${statisticComparisonSummary[0]?.ageGroup} 여성"
    val mySleepREMAverage = statisticSummary?.averageRemSleepMinutes?.toInt() ?: 0
    val myREMRatio = statisticSummary?.averageRemSleepPercentage ?: 0
    val otherSleepREMAverage = statisticComparisonSummary[0]?.remSleepMinutes?.toInt() ?: 0
    val otherREMRatio: Int = statisticComparisonSummary[0]?.remSleepRatio ?: 0
    val averageDifference = if (mySleepREMAverage> otherSleepREMAverage) true else false
    LazyColumn(
        modifier = modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom =50.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                ComparisonAverage(
                    modifier = modifier,
                    days = days,
                    sleepHours = statistics?.remSleep?.map { StatisticsSleepHour(it.value.toInt()) } ?: emptyList(),
                    title = stringResource(R.string.REM_sleep),
                    my_name = "내 평균",
                    my_value = mySleepREMAverage.toFloat(),
                    other_name = "${other} 평균",
                    other_value = otherSleepREMAverage.toFloat()
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="${other} 평균",
                    before_value= otherSleepREMAverage.toFloat(),
                    after_name = "내평균",
                    after_value = mySleepREMAverage.toFloat(),
                    title = {
                        White_text("${other} 평균 렘 수면 시간")
                        Comparison_text(blue_text = "${SummarTime(otherSleepREMAverage)}", white_text = "보다")
                        Comparison_text(blue_text = "평균 ${Math.abs(mySleepREMAverage - otherSleepREMAverage)}분", white_text = if (averageDifference) "더 주무셨어요" else "덜 주무셨어요")
                    }
                )
                ComparisonChart(
                    modifier = modifier,
                    before_name="${other} 평균",
                    before_value= (otherREMRatio.toFloat()),
                    after_name = "내 평균",
                    after_value = (myREMRatio.toFloat()),
                    title = {
                        White_text("${other} 평균 렘 수면 비율")
                        Comparison_text(blue_text = "${otherREMRatio}%" , white_text = "보다")
                        Comparison_text(blue_text = "${Math.abs(myREMRatio - otherREMRatio)}%", white_text = if (myREMRatio < otherREMRatio) "더 적게 주무셨어요" else "더 많이 주무셨어요" )
                    }
                )
                Help_comment(
                    modifier = modifier,
                    title= {
                        White_text("REM 수면이 부족하면 \n"+"다음과 같은 문제가 생길 수 있어요!!")
                    },
                    value1_image = painterResource(R.drawable.brain_icon),
                    value1_title = "기억력 저하",
                    value1_content = "REM 수면은 뇌 정보를 정리하고 저장하는 단계로 부족하면 학습 능력과 기억력이 저하될 수 있습니다",
                    value2_image = painterResource(R.drawable.cry_icon),
                    value2_title = "정서적 불안정",
                    value2_content= "스트레스나 불안감이 증가하고 감정적인 안정이 어려워질 수 있습니다 ",
                    value3_image= painterResource(R.drawable.idea_icon),
                    value3_title= "창의성 감소",
                    value3_content = "창의적 사고와 문제 해결능력은 REM 수면 동안 활성화됩니다. 부족하면 창의력이 떨어질수 있습니다"
                )
            }
        }
    }
}

@Composable
fun Help_comment(
    modifier: Modifier,
    title: @Composable () -> Unit,
    value1_image:Painter,
    value1_title:String,
    value1_content:String,
    value2_image:Painter,
    value2_title:String,
    value2_content:String,
    value3_image:Painter,
    value3_title:String,
    value3_content:String,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
            title()
            Box(
                modifier = modifier
                    .background(
                        color = Color.Black.copy(alpha = .3f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = modifier
                        .padding(10.dp, 0.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp,Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = value1_image,
                        contentDescription = "",
                        modifier = modifier.size(60.dp)
                    )
                    Column(
                        modifier = modifier.padding(0.dp,10.dp)
                    ) {
                        Text(
                            text = value1_title,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = value1_content,
                            color = Color.White.copy(alpha = .5f),
                            fontSize = 15.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
            Box(
                modifier = modifier
                    .background(
                        color = Color.Black.copy(alpha = .3f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = modifier
                        .padding(10.dp, 0.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp,Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = value2_image,
                        contentDescription = "",
                        modifier = modifier.size(60.dp)
                    )
                    Column(
                        modifier = modifier.padding(0.dp,10.dp)
                    ) {
                        Text(
                            text = value2_title,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = value2_content,
                            color = Color.White.copy(alpha = .5f),
                            fontSize = 15.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
            Box(
                modifier = modifier
                    .background(
                        color = Color.Black.copy(alpha = .3f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = modifier
                        .padding(10.dp, 0.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp,Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = value3_image,
                        contentDescription = "",
                        modifier = modifier.size(60.dp)
                    )
                    Column(
                        modifier = modifier.padding(0.dp,10.dp)
                    ) {
                        Text(
                            text = value3_title,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = value3_content,
                            color = Color.White.copy(alpha = .5f),
                            fontSize = 15.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
