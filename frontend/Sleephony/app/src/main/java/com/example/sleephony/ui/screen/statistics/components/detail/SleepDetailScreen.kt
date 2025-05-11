package com.example.sleephony.ui.screen.statistics.components.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sleephony.R

@Composable
fun SleepDetailScreen(
    page: String,
    navController: NavController,
    days:List<String>,
) {
    val modifier = Modifier
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF182741),
                        Color(0xFF314282),
                        Color(0xFF1D1437)
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(R.drawable.bg_stars),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = modifier.padding(start = 20.dp, end = 20.dp)
        ) {
            DetailTopBar(
                modifier = modifier,
                page = page,
                navController = navController
            )
            when(page) {
                "sleep_time" -> SleepTimeDetailScreen(modifier = modifier, days= days)
                "sleep_latency" -> SleepLatencyDetailScreen(modifier = modifier, days= days)
                "sleep_REM" -> SleepREMDetailScreen(modifier =  modifier, days= days)
                "sleep_light" -> SleepLightDetailScreen(modifier = modifier, days= days)
                "sleep_deep" -> SleepDeepDetailScreen(modifier = modifier, days= days)
            }
        }
    }
}