package com.example.sleephony_wear.components.alarm

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.sleepphony_wear_os.R
import com.example.sleepphony_wear_os.presentation.theme.darkGray
import com.example.sleepphony_wear_os.presentation.theme.darkNavyBlue

@Composable
fun AlarmCheckScreen(
    modifier: Modifier,
    navController: NavController
) {
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
                    Text(text = "오후", fontSize = 25.sp)
                    Text(text = "11 : 30", fontSize = 25.sp)
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp,0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "오전",fontSize = 25.sp)
                    Text(text = "08 : 30",fontSize = 25.sp)
                }
                Button(
                    modifier = modifier
                        .fillMaxWidth(.5f)
                        .height(30.dp)
                        .padding(bottom = 5.dp),
                    colors = ButtonDefaults.buttonColors(darkNavyBlue),
                    onClick = {}
                ) {
                    Text(text = stringResource(R.string.check))
                }
            }
        }
    }
}
