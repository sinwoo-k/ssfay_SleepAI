package com.example.sleephony.ui.screen.statistics.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sleephony.R
import com.example.sleephony.ui.screen.statistics.components.detail.Gray_text
import com.example.sleephony.ui.screen.statistics.week.WeekChart

@Composable
fun DetailSleep(
    modifier: Modifier,
    navController: NavController,
    title:@Composable ()-> Unit,
    days:List<String>,
    sleepHours:List<Float>,
    path:String,
    period:String
) {
   val isValue = sleepHours.any {it != 0.0f}
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        title()
        Box(modifier = modifier
            .padding(3.dp)
            .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .height(200.dp)
            .clickable {
                if (isValue) {
                    navController.navigate(route = "detail/$path/$period")
                }
            }
        ) {
            Row(
                modifier = modifier.fillMaxWidth()
                    .padding(end = 15
                        .dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (isValue){
                    Image(
                        painter = painterResource(R.drawable.more_icon),
                        contentDescription = "더보기 아이콘",
                        modifier = modifier.size(30.dp),
                    )
                }
            }
            if (!isValue) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.fillMaxSize().padding(10.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.sleep_icon),
                        contentDescription = "수면 아이콘",
                        modifier = modifier.size(100.dp)
                    )
                    Text(
                        text = stringResource(R.string.statisticIsEmpty),
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = stringResource(R.string.statisticIsEmpty2),
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                WeekChart(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp),
                    days = days, sleepHours = sleepHours)
            }

        }
    }
}

