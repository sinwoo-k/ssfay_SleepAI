package com.example.sleephony.ui.screen.statistics.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.sleephony.ui.screen.statistics.components.detail.White_text

@Composable
fun AverageSleepScore(
    modifier: Modifier = Modifier,
    averageSleepScore: Int,
    averageSleepTimeMinutes: Int,
    averageSleepLatencyMinutes: Int
) {
    val alphaWhite = Color.White.copy(alpha = .7f)
    val isOpen = remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.sleep_score),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(20.dp))
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.average_sleep_score),
                        fontSize = 20.sp,
                        color = alphaWhite
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "${averageSleepScore}점",
                            fontSize = 20.sp,
                            color = alphaWhite
                        )
                        Image(
                            modifier = Modifier.size(15.dp).clickable {
                                isOpen.value = true
                            },
                            painter = painterResource(R.drawable.question),
                            contentDescription = "물음표 아이콘"
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .background(
                            color = colorResource(R.color.steel_blue),
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = averageSleepScore / 100f)
                            .height(25.dp)
                            .background(
                                color = colorResource(R.color.SkyBlue),
                                shape = RoundedCornerShape(20.dp)
                            )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.average_sleep_time),
                        fontSize = 20.sp,
                        color = alphaWhite
                    )
                    Text(
                        text = parsingTime(averageSleepTimeMinutes),
                        fontSize = 20.sp,
                        color = alphaWhite
                    )
                }

                Divider(color = alphaWhite, thickness = 1.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.average_sleep_latency),
                        fontSize = 20.sp,
                        color = alphaWhite
                    )
                    Text(
                        text = parsingTime(averageSleepLatencyMinutes),
                        fontSize = 20.sp,
                        color = alphaWhite
                    )
                }
            }
        }
    }
    BottomSheetAlert(
        showSheet = isOpen.value,
        onDismiss = {isOpen.value = false},
        content = {
            Column(
                modifier = modifier.fillMaxWidth().padding(bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    White_text("수면 점수")
                }
                Text(
                    text = stringResource(R.string.average_sleep_score_help),
                    color = Color.White

                )
            }
        }
    )
}


fun parsingTime(value: Int): String {
    if (value == 0) return "0분"

    val hour = value / 60
    val min = value % 60

    return if (hour != 0) "${hour}시간 ${min}분" else "${min}분"
}