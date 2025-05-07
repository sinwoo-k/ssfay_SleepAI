package com.example.sleephony.ui.screen.report.week

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.sleephony.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun WeekSleepTime(
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = stringResource(R.string.sleep_time), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 30.sp)
        Box(modifier = modifier
            .padding(3.dp)
            .background(color = Color.Black.copy(alpha = .3f), shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .height(200.dp)
        ) {
            val days = remember { listOf("월", "화", "수", "목", "금", "토", "일") }
            val sleepHours = remember { listOf(7.5f, 6.8f, 8.2f, 7.0f, 6.5f, 8.5f, 9.0f) }
            WeekChart(modifier = Modifier.fillMaxSize(), days = days, sleepHours = sleepHours)
        }
    }
}

