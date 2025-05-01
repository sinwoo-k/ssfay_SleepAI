package com.example.sleephony_wear.presentation.components.step

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sleepphony_wear_os.presentation.theme.backWhite

@Composable
fun StepIndicator(
    modifier: Modifier = Modifier,
    step:Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .padding(horizontal = 4.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(color = if (step == 1) Color.White else backWhite)
        )
        Box(
            Modifier
                .padding(horizontal = 4.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(color = if (step == 2) Color.White else backWhite)
        )
        Box(
            Modifier
                .padding(horizontal = 4.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(color = if (step == 3) Color.White else backWhite)
        )
    }
}