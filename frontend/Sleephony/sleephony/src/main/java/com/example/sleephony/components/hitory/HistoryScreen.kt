package com.example.sleephony.presentation.components.hitory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.sleephony.R
import com.example.sleephony.presentation.theme.backWhite
import com.example.sleephony.presentation.theme.darkGray


@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    ScalingLazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        reverseLayout = true
    ) {
        item {
            Button(
                modifier = modifier.fillMaxWidth(0.9f)
                    .padding(3.dp)
                    .height(30.dp),
                colors = ButtonDefaults.buttonColors(darkGray),
                onClick = {}
            ) { Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                        painter = painterResource(id = R.drawable.moon),
                        contentDescription = "달 아이콘",
                        tint = Color.White,
                        modifier = modifier.size(20.dp)
                    )
                Text(text = "11월 28일", fontSize = 12.sp)
                Text(text = "12시간 34분",fontSize = 12.sp, color = backWhite)
            } }
        }
    }
}