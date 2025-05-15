package com.example.sleephony.ui.screen.report.component.ai

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ScoreGauge(score: Int, maxScore: Int, width: Dp, height: Dp) {
    val progress = remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(targetValue = progress.value, label = "progress")

    LaunchedEffect(score) {
        delay(320)
        progress.value = score / maxScore.toFloat()
    }

    val strokeWidth = 20.dp

    Box(
        modifier = Modifier
            .width(width)
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val angle = 120f
            val startAngle = 210f

            drawArc(
                color = Color.Gray.copy(alpha = 0.3f),
                startAngle = startAngle,
                sweepAngle = angle,
                useCenter = false,
                size = size,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color = Color(0xFF80B6FF),
                startAngle = startAngle,
                sweepAngle = angle * animatedProgress,
                useCenter = false,
                size = size,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}
