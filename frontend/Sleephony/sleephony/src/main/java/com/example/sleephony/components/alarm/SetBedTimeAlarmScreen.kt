package com.example.sleephony_wear.components.alarm

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.rememberPickerState
import com.example.sleephony.R
import com.example.sleephony.presentation.theme.darkNavyBlue

@Composable
fun SetBedTimeAlarmScreen(
    modifier: Modifier = Modifier,
    navController: NavController
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
    val meridiemState = rememberPickerState(initialNumberOfOptions = meridiems.size, repeatItems = false)

    val hour = hourState.selectedOption
    val minute = minuteState.selectedOption
    val meridiem = meridiemState.selectedOption

    val focusMeridiem = remember { FocusRequester() }
    val focusHour = remember { FocusRequester() }
    val focusMinute = remember { FocusRequester() }

    LaunchedEffect(hour,minute,meridiem) {
        onTime(meridiems[meridiem],hour,minute)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Picker(
            state = meridiemState,
            modifier = Modifier.size(60.dp, 80.dp)
                .padding(top = 17.dp)
                .clickable { focusMeridiem.requestFocus() }
                .focusRequester(focusMeridiem),
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
            Text(
                text = "시",
                modifier = Modifier.size(20.dp)
            )
            Picker(
                state = hourState,
                modifier = Modifier.size(60.dp, 80.dp)
                    .clickable { focusHour.requestFocus() }
                    .focusRequester(focusHour),
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
            Text(
                text = "분",
                modifier = Modifier.size(20.dp),
                )
            Picker(
                state = minuteState,
                modifier = Modifier.size(60.dp, 80.dp)
                    .clickable { focusMinute.requestFocus() }
                    .focusRequester(focusMinute),
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
