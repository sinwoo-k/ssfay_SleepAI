package com.example.sleephony.ui.screen.statistics.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.sleephony.R

@Composable
fun ReportTopBar (
    modifier: Modifier,
    step:Int,
    onChange:(Int)->Unit
) {
    Row(
        modifier = modifier
            .padding(top=5.dp, start = 15.dp, end = 15.dp)
            .fillMaxWidth()
            .background(color = Color.Transparent, shape = RoundedCornerShape(20.dp)),
    ) {
        Button(
            modifier = modifier
                .weight(1f)
                .padding(5.dp,0.dp),
            onClick = {onChange(1)},
            colors =  ButtonDefaults.outlinedButtonColors(
                containerColor = if (step == 1) Color.White.copy(alpha = 0.2f) else Color.Transparent
            ),
            border = if (step == 1)
                BorderStroke(2.dp, Color.White)
            else
                BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
        ) { Text(text = "1주",color = Color.White) }
        Button(
            modifier = modifier
                .weight(1f)
                .padding(5.dp,0.dp),
            onClick = {onChange(2)},
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (step == 2) Color.White.copy(alpha = 0.2f) else Color.Transparent
            ),
            border = if (step == 2)
                BorderStroke(2.dp, Color.White)
            else
                BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
        ) { Text(text = "1개월", color = Color.White) }
        Button(
            modifier = modifier
                .weight(1f)
                .padding(5.dp,0.dp),
            onClick = {onChange(3)},
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (step == 3) Color.White.copy(alpha = 0.2f) else Color.Transparent
            ),
            border = if (step == 3)
                BorderStroke(2.dp, Color.White)
            else
                BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
        ) { Text(text = "1년", color = Color.White) }
    }
}