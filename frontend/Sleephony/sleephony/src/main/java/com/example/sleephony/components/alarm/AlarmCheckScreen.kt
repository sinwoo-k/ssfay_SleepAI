package com.example.sleephony.components.alarm

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.sleephony.R
import com.example.sleephony.presentation.theme.darkGray
import com.example.sleephony.presentation.theme.darkNavyBlue
import com.example.sleephony.utils.WearMessageUtils
import com.example.sleephony.viewmodel.AlarmViewModel

@Composable
fun AlarmCheckScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: AlarmViewModel,
) {
    val context = LocalContext.current

    val bedMeridiemState = viewModel.bedMeridiem.observeAsState("")
    val bedHourState = viewModel.bedHour.observeAsState("")
    val bedMinuteState = viewModel.bedMinute.observeAsState("")
    val wakeUpMeridiemState = viewModel.wakeUpMeridiem.observeAsState("")
    val wakeUpHourState = viewModel.wakeUpHour.observeAsState("")
    val wakeUpMinuteState = viewModel.wakeUpMinute.observeAsState("")

    val bedMeridiem = context.getString(R.string.bed_meridiem, bedMeridiemState.value)
    val bedHour = bedHourState.value
    val bedMinute = bedMinuteState.value
    val wakeUpMeridiem = context.getString(R.string.wakeup_meridiem, wakeUpMeridiemState.value)
    val wakeUpHour = wakeUpHourState.value
    val wakeUpMinute = wakeUpMinuteState.value
    val alarmType = viewModel.alarmType.value

    fun requestBatteryOptimizationExemption() {
        try {
            val packageName = context.packageName
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:$packageName")
                }
                context.startActivity(intent)
                Log.e("ssafy", "배터리 최적화 예외 설정 성공")
            } else {
                Toast.makeText(context, "배터리 최적화가 이미 비활성화되어 있습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // 로그에 오류 출력
            Log.e("ssafy", "배터리 최적화 예외 설정 실패: ${e.message}")
            Toast.makeText(context, "배터리 최적화 설정에 문제가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ion_alarm),
            contentDescription = "시계 아이콘",
            modifier = modifier.zIndex(1f)
        )
        Box(
            modifier = modifier
                .fillMaxWidth(.8f)
                .fillMaxHeight()
                .offset(y = (-15).dp)
                .clip(RoundedCornerShape(25.dp))
                .background(darkGray),
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically),
                modifier = modifier
                    .fillMaxSize()
            ) {
                Row(modifier = modifier.fillMaxWidth()
                    .padding(8.dp,0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = bedMeridiem, fontSize = 25.sp)
                    Text(text = "$bedHour : $bedMinute", fontSize = 25.sp)
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp,0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = wakeUpMeridiem,fontSize = 25.sp)
                    Text(text = "$wakeUpHour : $wakeUpMinute",fontSize = 25.sp)
                }
                Button(
                    modifier = modifier
                        .fillMaxWidth(.5f)
                        .height(30.dp)
                        .padding(bottom = 5.dp),
                    colors = ButtonDefaults.buttonColors(darkNavyBlue),
                    onClick = {
                        requestBatteryOptimizationExemption()
                        WearMessageUtils.SendMessage(
                            context = context,
                            mode = "alarm",
                            data = mapOf(
                                "wakeUpTime" to "$wakeUpMeridiem $wakeUpHour $wakeUpMinute",
                                "alarmType" to alarmType
                            )
                        )
                        navController.navigate("sleepingscreen")
                    }
                ) {
                    Text(text = stringResource(R.string.check))
                }
            }
        }
    }
}

