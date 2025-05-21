package com.example.sleephony.ui.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat

@Composable
fun TimeWheelPicker(
    initialHour: Int = 6,
    initialMinute: Int = 30,
    initialIsAm: Boolean = true,
    onTimeChanged: (hour: Int, minute: Int, isAm: Boolean) -> Unit
) {
    var hour by rememberSaveable { mutableStateOf(initialHour) }
    var minute by rememberSaveable { mutableStateOf(initialMinute) }
    var isAm by rememberSaveable { mutableStateOf(initialIsAm) }

    LaunchedEffect(hour, minute, isAm) {
        onTimeChanged(hour, minute, isAm)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {

        WheelTimePicker(
            timeFormat = TimeFormat.AM_PM,
            size = DpSize(500.dp, 200.dp),
            textColor = Color.White,
            textStyle = MaterialTheme.typography.titleLarge
        ){snappedTime ->
            val selectHour24 = snappedTime.hour
            val selectMinute = snappedTime.minute
            val selectIsAm   = selectHour24 < 12

            val selectHour12 = when {
                selectHour24 == 0  -> 12
                selectHour24 > 12  -> selectHour24 - 12
                else         -> selectHour24
            }
            onTimeChanged(selectHour12, selectMinute, selectIsAm)
        }
    }
}