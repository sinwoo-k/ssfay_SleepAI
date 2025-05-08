package com.example.sleephony.ui.screen.report.components.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
    modifier: Modifier,
    page: String,
    navController: NavController
) {
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
            modifier = modifier.padding(top = 35.dp, start = 10.dp, end = 10.dp, bottom =50.dp)
        ) {
            DetailTopBar(
                modifier = modifier,
                page = page,
                navController = navController
            )
            when(page) {
                "sleep_time" -> SleepTimeDetailScreen(modifier = modifier)
                "sleep_latency" -> SleepLatencyDetailScreen()
                "sleep_REM" -> SleepREMDetailScreen()
                "sleep_light" -> SleepLightDetailScreen()
                "sleep_deep" -> SleepDeepDetailScreen()
            }
        }
    }
}