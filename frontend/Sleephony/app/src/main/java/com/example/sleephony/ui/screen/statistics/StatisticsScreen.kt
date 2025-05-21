package com.example.sleephony.ui.screen.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sleephony.R
import com.example.sleephony.ui.screen.statistics.components.ReportTopBar
import com.example.sleephony.ui.screen.statistics.month.MonthReport
import com.example.sleephony.ui.screen.statistics.viewmodel.StatisticsViewModel
import com.example.sleephony.ui.screen.statistics.week.WeekReport
import com.example.sleephony.ui.screen.statistics.year.YearReport

@Composable
fun StatisticsScreen(
    modifier: Modifier,
    navController: NavController,
    statisticsViewModel:StatisticsViewModel
) {
    var step = statisticsViewModel.step
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
        Column(modifier = modifier.padding( start = 20.dp, end = 20.dp)) {
            ReportTopBar(modifier = modifier, step = step, onChange = {newStep -> statisticsViewModel.step =newStep})
            when (step) {
                1 -> WeekReport(modifier = modifier, navController = navController, statisticsViewModel = statisticsViewModel)
                2 -> MonthReport(modifier = modifier, navController = navController, statisticsViewModel = statisticsViewModel)
                3 -> YearReport(modifier = modifier, navController = navController, statisticsViewModel = statisticsViewModel)
            }
        }
    }
}