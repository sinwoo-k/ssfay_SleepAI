package com.example.sleephony_wear.components.alarm

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Scaffold
import com.example.sleephony_wear.presentation.components.step.StepIndicator
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SetAlarmScreen(
    modifier: Modifier,
    navController: NavController
) {
    val pageState = rememberPagerState()
    val stepNum = remember { mutableStateOf(1) }

    LaunchedEffect(pageState.currentPage) {
        stepNum.value = pageState.currentPage + 1
    }

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
        ) {
            StepIndicator(step = stepNum.value)

            HorizontalPager(
                count = 3,
                modifier = modifier.fillMaxSize(),
                state = pageState
            ) { page ->
                when (page) {
                    0 -> SetBedTimeAlarmScreen(modifier = modifier, navController = navController)
                    1 -> SetWakeUpTimeAlarmScreen(modifier = modifier, navController = navController)
                    2 -> AlarmCheckScreen(modifier = modifier, navController = navController)
                }
            }
        }
    }
}
