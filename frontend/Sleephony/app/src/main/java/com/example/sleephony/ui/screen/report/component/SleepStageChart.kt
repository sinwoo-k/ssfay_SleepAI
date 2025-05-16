package com.example.sleephony.ui.screen.report.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.sleephony.ui.screen.report.viewmodel.SleepStage
import com.example.sleephony.ui.screen.report.viewmodel.SleepStageBlock
import java.time.LocalDateTime
import java.time.Duration

@Composable
fun SleepStageChart(
    blocks: List<SleepStageBlock>,
    sleepStartTime: LocalDateTime,
    sleepEndTime: LocalDateTime,
    modifier: Modifier = Modifier
) {
    val totalDuration = Duration.between(sleepStartTime, sleepEndTime).seconds.toFloat()

    Column(modifier = modifier.fillMaxWidth()) {
        SleepStage.values().forEach { stage ->
            val stageBlocks = blocks.filter { it.stage == stage }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stage.label,
                    color = stage.color,
                    modifier = Modifier.width(80.dp)
                )

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    val totalWidthPx = constraints.maxWidth.toFloat()
                    val density = LocalDensity.current

                    stageBlocks.forEach { block ->
                        val startSec = Duration.between(sleepStartTime, block.start).seconds.toFloat()
                        val endSec = Duration.between(sleepStartTime, block.end).seconds.toFloat()

                        val durationSec = endSec - startSec
                        if (durationSec <= 0f || totalDuration <= 0f) return@forEach

                        val startPx = totalWidthPx * (startSec / totalDuration)
                        val widthPx = totalWidthPx * (durationSec / totalDuration)
                        val safeWidthPx = (startPx + widthPx).coerceAtMost(totalWidthPx) - startPx

                        with(density) {
                            Box(
                                modifier = Modifier
                                    .absoluteOffset(x = startPx.toDp())
                                    .width(safeWidthPx.toDp())
                                    .fillMaxHeight()
                                    .background(block.color, shape = RoundedCornerShape(4.dp))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
