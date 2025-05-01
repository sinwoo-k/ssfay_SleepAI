package com.example.sleephony_wear.components.alarm

import android.content.Context
import android.util.Log
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
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AlarmCheckScreen(
    modifier: Modifier,
    navController: NavController
) {
    val context = LocalContext.current
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
                    onClick = {
                        SendMessage(context)
                    }
                ) {
                    Text(text = stringResource(R.string.check))
                }
            }
        }
    }
}

fun SendMessage(context:Context){
    CoroutineScope(Dispatchers.IO).launch {
        try{
            val nodeClient = Wearable.getNodeClient(context)
            val messageClient = Wearable.getMessageClient(context)

            val nodes = nodeClient.connectedNodes.await()
            for (node in nodes) {
                Log.d("ssafy", "노드 ID: ${node.id}")
                messageClient.sendMessage(
                    node.id,
                    "/alarm",
                    "hello, ssafy".toByteArray()
                ).await()
                Log.d("ssafy","메시지 보냄")
            }
        } catch (error: Exception){
            Log.e("ssafy","$error")
        }
    }
}