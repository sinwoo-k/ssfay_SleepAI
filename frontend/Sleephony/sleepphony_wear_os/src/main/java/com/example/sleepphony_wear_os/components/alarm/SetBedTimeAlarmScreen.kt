package com.example.sleephony_wear.components.alarm

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.rememberPickerState
import com.example.sleepphony_wear_os.R
import com.example.sleepphony_wear_os.presentation.theme.darkNavyBlue

@Composable
fun SetBedTimeAlarmScreen(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

            Text(text = stringResource(id = R.string.sleep_time), fontSize = 18.sp)

            SelectTime() {meridiem, hour, minute -> Log.d("TAG","$meridiem-$hour:$minute") }

            Button(
                modifier = modifier
                    .fillMaxWidth(0.5f)
                    .height(25.dp),
                onClick = {

            },
            colors = ButtonDefaults.buttonColors(darkNavyBlue)
            ) {
                Text("다음")
        }
    }
}

@Composable
fun SelectTime(onTime:(meridiem:String,hour:Int,minute:Int) -> Unit){
    val meridiems = listOf(stringResource(id = R.string.am), stringResource(id = R.string.pm))
    val hours = (0..11).map { it.toString().padStart(2, '0') }
    val minutes = (0..59).map { it.toString().padStart(2, '0') }

    val hourState = rememberPickerState(initialNumberOfOptions = hours.size)
    val minuteState = rememberPickerState(initialNumberOfOptions = minutes.size)
    val meridiemState = rememberPickerState(initialNumberOfOptions = meridiems.size)

    val hour = hourState.selectedOption
    val minute = minuteState.selectedOption
    val meridiem = meridiemState.selectedOption


    LaunchedEffect(hour,minute,meridiem) {
        onTime(meridiems[meridiem],hour,minute)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Picker(
            state = meridiemState,
            modifier = Modifier.size(60.dp, 80.dp)
                .padding(top = 15.dp),
            contentDescription = "오전/오후",
            separation = 7.dp,
            gradientRatio = 0f,
        )
        {
            val isSelected = it == meridiemState.selectedOption
            Text(
                text = meridiems[it],
                fontSize = if (isSelected) 22.sp else 16.sp,
                color = if (isSelected) Color.White else Color.Gray
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "시")
            Picker(
                state = hourState,
                modifier = Modifier.size(60.dp, 80.dp),
                contentDescription = "시간",
                separation = 7.dp,
                gradientRatio = 0f
            )
            {
                val isSelected = it == hourState.selectedOption
                Text(
                    text = hours[it],
                    fontSize = if (isSelected) 22.sp else 16.sp,
                    color = if (isSelected) Color.White else Color.Gray
                )
            }

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "분")
            Picker(
                state = minuteState,
                modifier = Modifier.size(60.dp, 80.dp),
                contentDescription = "분",
                separation = 7.dp,
                gradientRatio = 0f
            ) {
                val isSelected = it == minuteState.selectedOption
                Text(
                    text = minutes[it],
                    fontSize = if (isSelected) 22.sp else 16.sp,
                    color = if (isSelected) Color.White else Color.Gray
                )
            }
        }

    }
}
