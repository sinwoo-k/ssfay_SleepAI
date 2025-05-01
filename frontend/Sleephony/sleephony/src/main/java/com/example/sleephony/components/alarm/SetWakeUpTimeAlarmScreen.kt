package com.example.sleephony_wear.components.alarm

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.sleephony.R
import com.example.sleephony.presentation.theme.darkNavyBlue


@Composable
fun SetWakeUpTimeAlarmScreen(
    modifier: Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.wakeup_time))

        SelectTime() {meridiem, hour, minute -> Log.d("TAG","$meridiem-$hour:$minute") }

        Button(
            modifier = modifier
                .fillMaxWidth(0.5f)
                .height(25.dp),
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(darkNavyBlue
            )
        ) {
            Text("다음")
        }
    }
}