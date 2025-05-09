package com.example.sleephony.ui.screen.report.components.detail.sleepcycle.sleeptime

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