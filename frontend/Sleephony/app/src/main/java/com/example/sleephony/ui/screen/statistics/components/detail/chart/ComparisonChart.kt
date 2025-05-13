package com.example.sleephony.ui.screen.statistics.components.detail.chart

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.sleephony.R
import com.example.sleephony.ui.screen.statistics.components.detail.SummarTime
import com.example.sleephony.ui.screen.statistics.week.StatisticsSleepHour

@Composable
fun ComparisonChart(
    modifier: Modifier,
    before_name:String,
    before_value:Float,
    after_name : String,
    after_value : Float,
    title: @Composable () -> Unit
) {
    fun change(value: Float): Float {
        val length = value.toInt().toString().length
        when (length) {
            2 -> return ((value / 100) * 300)
            3 -> return ((value / 1000) * 300)
            4 -> return ((value / 10000) * 300)
            else -> return .0f
        }
    }
    Box(modifier = modifier.fillMaxWidth()) {
        Column {
            title()
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.Black.copy(alpha = .3f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            20.dp,
                            Alignment.CenterHorizontally
                        ),
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                5.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = modifier.size(15.dp).background(
                                    color = colorResource(R.color.steel_blue),
                                    shape = RoundedCornerShape(50.dp)
                                )
                            )
                            Text(text = "$before_name", color = Color.White.copy(alpha = .3f))
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                5.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = modifier.size(15.dp).background(
                                    color = colorResource(R.color.SkyBlue),
                                    shape = RoundedCornerShape(50.dp)
                                )
                            )
                            Text(text = "$after_name", color = Color.White)
                        }
                    }
                    Row(
                        modifier = modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${SummarTime(before_value.toInt())}", color = Color.White.copy(alpha = .3f))
                        Box(
                            modifier = Modifier
                                .width(change(before_value).dp)
                                .height(30.dp)
                                .background(
                                    color = colorResource(R.color.steel_blue),
                                    shape = RoundedCornerShape(10.dp)
                                )
                        )
                    }

                    Row(
                        modifier = modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${SummarTime(after_value.toInt())}", color = Color.White)
                        Box(
                            modifier = Modifier
                                .width(change(after_value).dp)
                                .height(30.dp)
                                .background(
                                    color = colorResource(R.color.SkyBlue),
                                    shape = RoundedCornerShape(10.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}
