package com.example.sleephony_wear.components.alarm

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.sleephony.R
import com.example.sleephony.presentation.theme.darkNavyBlue
import com.example.sleephony.viewmodel.AlarmViewModel


@Composable
fun SetWakeUpTimeAlarmScreen(
    modifier: Modifier,
    navController: NavController,
    onNext: () -> Unit,
    viewModel: AlarmViewModel
) {
    val wakeUpMeridiem = remember { mutableStateOf("") }
    val wakeUpHour = remember { mutableStateOf("") }
    val wakeUpMinute = remember { mutableStateOf("") }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.wakeup_time))

        SelectTime() {meridiem, hour, minute ->
            wakeUpMeridiem.value = meridiem
            wakeUpHour.value = hour.toString().padStart(2,'0')
            wakeUpMinute.value = minute.toString().padStart(2,'0')
        }

        Button(
            modifier = modifier
                .fillMaxWidth(0.5f)
                .height(25.dp),
            onClick = {
                viewModel.wakeUpUpdate(wakeUpMeridiem.value,wakeUpHour.value,wakeUpMinute.value)
                onNext()
            },
            colors = ButtonDefaults.buttonColors(darkNavyBlue
            )
        ) {
            Text("다음")
        }
    }
}