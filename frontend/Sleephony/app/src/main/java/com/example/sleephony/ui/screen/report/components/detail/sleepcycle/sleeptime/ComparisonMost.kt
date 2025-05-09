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
fun  ComparisonMost(
    modifier: Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "가장 많이 잔날과 적게 잔날의",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                ) {
                    append("차이는")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 40.sp, fontWeight = FontWeight.Bold, color = colorResource(
                            R.color.SkyBlue
                        )
                    )
                ) {
                    append(" 2시간 ")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                ) {
                    append("이예요")
                }
            })
            ComparisonChart(
                modifier = modifier,
                before_name="최단 수면",
                before_value= 603f,
                after_name = "최장 수면",
                after_value = 803f
            )
        }
    }
}
