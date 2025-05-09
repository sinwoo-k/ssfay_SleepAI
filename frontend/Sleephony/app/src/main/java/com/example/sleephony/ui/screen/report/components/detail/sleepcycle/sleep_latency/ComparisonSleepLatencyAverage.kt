package com.example.sleephony.ui.screen.report.components.detail.sleepcycle.sleep_latency

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.report.components.detail.chart.ComparisonChart

@Composable
fun ComparisonSleepLatencyAverage(
    modifier: Modifier
){
    Box(modifier = modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "20대 남서 평균 잠복기인",
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
                    append("20분 ")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                ) {
                    append("범위 안에")
                }
            }
            )
            Text(text ="잘 주무셨네요!!", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White )
            ComparisonChart(
                modifier = modifier,
                before_name="저번주",
                before_value= 20f,
                after_name = "이번주",
                after_value = 14f
            )
        }
    }
}