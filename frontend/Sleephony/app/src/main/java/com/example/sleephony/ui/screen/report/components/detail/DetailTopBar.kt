package com.example.sleephony.ui.screen.report.components.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sleephony.R

@Composable
fun DetailTopBar(
    modifier: Modifier,
    page:String,
    navController: NavController
) {
    val page_name = remember { mutableStateOf("") }
    when(page) {
        "sleep_time" -> page_name.value = stringResource(R.string.sleep_time)
        "sleep_latency" -> page_name.value = stringResource(R.string.non_sleep)
        "sleep_REM" -> page_name.value = stringResource(R.string.REM_sleep)
        "sleep_light" -> page_name.value = stringResource(R.string.light_sleep)
        "sleep_deep" -> page_name.value = stringResource(R.string.deep_sleep)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(7.dp),

    ) {
        Image(
            painter = painterResource(R.drawable.pre_icon),
            contentDescription = "이전 이미지",
            modifier = modifier
                .align(Alignment.CenterStart)
                .clickable {
                    navController.popBackStack()
                }
        )
        Text(
            text = "${page_name.value}",
            modifier = modifier.align(Alignment.Center),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp
        )
    }
}