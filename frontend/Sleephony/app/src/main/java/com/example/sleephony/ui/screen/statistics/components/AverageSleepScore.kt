package com.example.sleephony.ui.screen.statistics.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R
import com.example.sleephony.data.model.StatisticData
import com.example.sleephony.ui.screen.statistics.week.StatisticsSleepHour
import com.example.sleephony.ui.screen.statistics.week.StatisticsTime

@Composable
fun AverageSleepScore(
    modifier: Modifier,
    averageSleepScore:Int,
    averageSleepTimeMinutes:Int,
    averageSleepLatencyMinutes:Int
) {
    val alphaWhite = Color.White.copy(alpha = .7f)


    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = stringResource(R.string.sleep_score), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 30.sp)
        Box(modifier = modifier
            .padding(3.dp)
            .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.average_sleep_score),fontSize = 20.sp, color = alphaWhite)
                    Box {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(text = "${averageSleepScore}점", fontSize = 20.sp, color = alphaWhite)
                            Image(
                                modifier = modifier.size(15.dp),
                                painter = painterResource(R.drawable.question),
                                contentDescription = "물음표 아이콘"
                            )
                        }
                    }
                }
                Box(
                    modifier = modifier.padding(5.dp)
                        .height(25.dp)
                        .fillMaxWidth()) {
                    Box( modifier = modifier
                        .height(25.dp)
                        .fillMaxWidth()
                        .background(color = colorResource(R.color.steel_blue), shape = RoundedCornerShape(20.dp)))
                    Box(
                        modifier = modifier
                            .height(25.dp)
                            .fillMaxWidth( averageSleepScore/100f)
                            .background(color = colorResource(R.color.SkyBlue), shape = RoundedCornerShape(20.dp))
                    )
                }
                Row(modifier = modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.average_sleep_time),fontSize = 20.sp, color = alphaWhite)
                    Text(text = "${parsingTime(averageSleepTimeMinutes)}",fontSize = 20.sp, color = alphaWhite)
                }
                Divider(
                    thickness = 1.dp,
                    color = alphaWhite
                )
                Row(modifier = modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.average_sleep_latency),fontSize = 20.sp, color = alphaWhite)
                    Text(text = "${parsingTime(averageSleepLatencyMinutes)}",fontSize = 20.sp, color = alphaWhite)
                }
            }
        }
    }
}

fun parsingTime(value: Int): String {
    if (value == 0) return "0분"

    val hour = value / 60
    val min = value % 60

    return if (hour != 0) "${hour}시간 ${min}분" else "${min}분"
}