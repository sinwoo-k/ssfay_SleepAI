package com.example.sleephony.ui.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun AmPmToggle(
    isAm: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(end = 12.dp)
    ) {
        listOf("오전" to true, "오후" to false).forEach { (label, flag) ->
            ClickableText(
                text    = AnnotatedString(label),
                onClick = { onToggle(flag) },
                style   = TextStyle(
                    fontSize   = 24.sp,
                    color      = if (isAm == flag) Color.White else Color.Gray
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun TimeWheelPicker(
    hourRange: IntRange = 1..12,
    minuteRange: IntRange = 0..59,
    initialHour: Int = 6,
    initialMinute: Int = 30,
    initialIsAm: Boolean = true,
    visibleItemCount: Int = 3,
    itemHeight: Dp = 50.dp,
    onTimeChanged: (hour: Int, minute: Int, isAm: Boolean) -> Unit
) {
    var hour by rememberSaveable { mutableStateOf(initialHour) }
    var minute by rememberSaveable { mutableStateOf(initialMinute) }
    var isAm by rememberSaveable { mutableStateOf(initialIsAm) }

    // 변화가 있을 때만 호출
    LaunchedEffect(hour, minute, isAm) {
        onTimeChanged(hour, minute, isAm)
    }

    // initialIndex 계산
    val initHourIdx = (initialHour - hourRange.first)
        .coerceIn(0, hourRange.last - hourRange.first)
    val initMinIdx = (initialMinute - minuteRange.first)
        .coerceIn(0, minuteRange.last - minuteRange.first)

    Row(verticalAlignment = Alignment.CenterVertically) {
        AmPmToggle(isAm) { isAm = it }

        LoopingWheelPicker(
            items = hourRange.map { it.toString().padStart(2,'0') },
            initialIndex = initHourIdx,
            visibleItemCount = visibleItemCount,
            itemHeight = itemHeight,
            modifier = Modifier
                .weight(1f)
                .height(itemHeight * visibleItemCount),
        ) { selIdx ->
            hour = hourRange.elementAt(selIdx)
        }

        Box(
            modifier = Modifier.height(itemHeight * visibleItemCount),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text    = ":",
                color   = Color.White,
                style   = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 8.dp),

            )
        }


        LoopingWheelPicker(
            items = minuteRange.map { it.toString().padStart(2,'0') },
            initialIndex = initMinIdx,
            visibleItemCount = visibleItemCount,
            itemHeight = itemHeight,
            modifier = Modifier
                .weight(1f)
                .height(itemHeight * visibleItemCount),
        ) { selIdx ->
            minute = minuteRange.elementAt(selIdx)
        }
    }
}

