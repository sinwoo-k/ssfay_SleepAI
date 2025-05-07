package com.example.sleephony.components.alarm

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Scaffold
import com.example.sleephony.presentation.components.step.StepIndicator
import com.example.sleephony.viewmodel.AlarmViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SetAlarmScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: AlarmViewModel
) {
    val pageState = rememberPagerState()
    val stepNum = remember { mutableStateOf(1) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pageState.currentPage) {
        stepNum.value = pageState.currentPage + 1
    }

    val onNext: () -> Unit = {
        coroutineScope.launch {
            if (
                pageState.currentPage == 0
                && viewModel.bedMeridiem.value?.isNotEmpty() == true
                && viewModel.bedHour.value?.length == 2
                && viewModel.bedMinute.value?.length == 2
            ) {
                pageState.animateScrollToPage(pageState.currentPage + 1)
            } else if (
                pageState.currentPage == 1
                && viewModel.bedMeridiem.value?.isNotEmpty() == true
                && viewModel.bedHour.value?.length == 2
                && viewModel.bedMinute.value?.length == 2
                && viewModel.wakeUpMeridiem.value?.isNotEmpty() == true
                && viewModel.wakeUpHour.value?.length == 2
                && viewModel.wakeUpMinute.value?.length ==2
            ) {
                pageState.animateScrollToPage(pageState.currentPage+1)
            }
        }
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
                state = pageState,
                userScrollEnabled = pageState.targetPage != 2
            ) { page ->
                when (page) {
                    0 -> SetBedTimeAlarmScreen(modifier = modifier, navController = navController, onNext = onNext, viewModel = viewModel)
                    1 -> SetWakeUpTimeAlarmScreen(modifier = modifier, navController = navController, onNext = onNext, viewModel = viewModel)
                    2 -> AlarmCheckScreen(modifier = modifier, navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}
