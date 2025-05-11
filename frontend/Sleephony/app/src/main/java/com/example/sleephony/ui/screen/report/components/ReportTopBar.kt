package com.example.sleephony.ui.screen.report.components

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
            .padding(top = 50.dp, start = 15.dp, end = 15.dp)
            .fillMaxWidth()
            .background(color = colorResource(R.color.dark_blue), shape = RoundedCornerShape(20.dp)),
    ) {
        Button(
            modifier = modifier
                .weight(1f)
                .padding(5.dp,0.dp),
            onClick = {onChange(1)},
            colors = ButtonDefaults.buttonColors(colorResource(if (step == 1) R.color.RoyalBlue else R.color.dark_navy))
        ) { Text(text = "1주") }
        Button(
            modifier = modifier
                .weight(1f)
                .padding(5.dp,0.dp),
            onClick = {onChange(2)},
            colors = ButtonDefaults.buttonColors(colorResource(if (step == 2) R.color.RoyalBlue else R.color.dark_navy))
        ) { Text(text = "1개월") }
        Button(
            modifier = modifier
                .weight(1f)
                .padding(5.dp,0.dp),
            onClick = {onChange(3)},
            colors = ButtonDefaults.buttonColors(colorResource(if (step == 3) R.color.RoyalBlue else R.color.dark_navy))
        ) { Text(text = "1년") }
    }
}