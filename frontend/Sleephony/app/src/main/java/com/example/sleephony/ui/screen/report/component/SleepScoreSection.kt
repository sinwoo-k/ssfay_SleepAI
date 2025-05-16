package com.example.sleephony.ui.screen.report.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleephony.R

@Composable
fun SleepScoreSection(
    sleepDurationText: String,
    diffText: String,
    comment: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "어제 밤",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "총 $sleepDurationText",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.SkyBlue)
            )
            Text(
                text = comment,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = diffText,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.3f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

