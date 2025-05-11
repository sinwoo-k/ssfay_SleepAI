package com.example.sleephony.ui.screen.statistics.components

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sleephony.R
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
                navController.navigate(route = "detail/$path/$period")
            }
        ) {
            Row(
                modifier = modifier.fillMaxWidth()
                    .padding(end = 15
                        .dp),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(R.drawable.more_icon),
                    contentDescription = "더보기 아이콘",
                    modifier = modifier.size(30.dp),
                )
            }
            WeekChart(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp),
                days = days, sleepHours = sleepHours)

        }
    }
}

