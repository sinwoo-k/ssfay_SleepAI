package com.example.sleephony.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import java.time.LocalTime

@Composable
fun TimeWheelPicker(
    initialHour: Int,
    initialMinute: Int,
    initialIsAm: Boolean,
    onTimeChanged: (hour: Int, minute: Int, isAm: Boolean) -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp

    val pickerWidth = screenWidthDp * 0.9f
    val pickerHeight = screenHeightDp * 0.25f



    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        WheelTimePicker(
            startTime = LocalTime.of(if (initialIsAm && initialHour == 12) 0 else if (!initialIsAm && initialHour < 12) initialHour + 12 else initialHour, initialMinute),
            timeFormat = TimeFormat.AM_PM,
            size = DpSize(pickerWidth, pickerHeight),
            textColor = Color.White,
            textStyle = MaterialTheme.typography.titleLarge
        ){snappedTime ->
            val h24 = snappedTime.hour
            val m   = snappedTime.minute
            val am  = h24 < 12
            val h12 = when {
                h24 == 0   -> 12
                h24 > 12   -> h24 - 12
                else       -> h24
            }
            onTimeChanged(h12, m, am)
        }
    }
}