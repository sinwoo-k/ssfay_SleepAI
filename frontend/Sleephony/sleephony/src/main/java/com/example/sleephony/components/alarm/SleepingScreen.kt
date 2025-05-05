package com.example.sleephony.components.alarm

import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.example.sleephony.R
import com.example.sleephony.presentation.theme.darkNavyBlue
import com.example.sleephony.service.SleepAlarmService
import com.example.sleephony.viewmodel.AlarmViewModel
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun SleepingScreen(
    modifier: Modifier,
    viewModel: AlarmViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    val wakeUpMeridiemState = viewModel.wakeUpMeridiem.observeAsState("")
    val wakeUpHourState = viewModel.wakeUpHour.observeAsState("")
    val wakeUpMinuteState = viewModel.wakeUpMinute.observeAsState("")

    val wakeUpMeridiem = context.getString(R.string.wakeup_meridiem, wakeUpMeridiemState.value)
    val wakeUpHour = wakeUpHourState.value
    val wakeUpMinute = wakeUpMinuteState.value

    var currentTime = remember { mutableStateOf("") }

    var isCheck = remember { mutableStateOf(false) }
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    val wakeup = "$wakeUpMeridiem $wakeUpHour:$wakeUpMinute"
    var intent = Intent(context, SleepAlarmService::class.java).apply {
        putExtra("waketime","$wakeup")
    }
    ContextCompat.startForegroundService(context,intent)

    LaunchedEffect(Unit) {
        while (true) {
            val now = LocalTime.now()
            var formatter = DateTimeFormatter.ofPattern("a hh:mm")
            currentTime.value = now.format(formatter)
            isCheck.value = (wakeup == currentTime.value)
            delay(5000L)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) {
        val imageRes = if (!isCheck.value) R.drawable.ion_alarm else R.drawable.alarm2_icon
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = "알람 icon"
                )
                Text(text = "${currentTime.value}", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                DragToDismissAlarm(
                    onDismiss = {
                        intent = Intent(context,SleepAlarmService::class.java)
                        context.stopService(intent)
                        navController.navigate("homeScreen")
                },
                    vibrator = vibrator
            )
        }
    }
}

@Composable
fun DragToDismissAlarm(
    radius: Float = 110f,
    onDismiss: () -> Unit,
    vibrator: Vibrator,
) {
    var offset = remember { mutableStateOf(Offset.Zero) }
    var isDragging = remember { mutableStateOf(false) }
    val effect = VibrationEffect.createOneShot(20, 50)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        isDragging.value = true
                        vibrator.vibrate(effect)
                    },
                    onDragEnd = {
                        val distance = sqrt(offset.value.x * offset.value.x + offset.value.y * offset.value.y)
                        if (distance > radius) {
                            onDismiss()
                        }
                        offset.value = Offset.Zero
                        isDragging.value = false
                    }
                ) { change, dragAmount ->
                    change.consume()
                    offset.value += dragAmount
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // 배경 원
        Canvas(modifier = Modifier.size((radius * 2).dp)) {
            drawCircle(
                color = if (isDragging.value) Color.Red.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0f),
                radius = radius
            )
        }

        // 드래그 가능한 버튼
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt())
                }
                .size(64.dp)
                .clip(CircleShape)
                .background(darkNavyBlue)
                .border(4.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.close_icon),
                contentDescription = "close icon",
                modifier = Modifier.fillMaxSize(.4f)
            )
        }
    }
}