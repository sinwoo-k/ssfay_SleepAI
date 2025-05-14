package com.example.sleephony.ui.screen.report.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.sleephony.ui.screen.report.viewmodel.SleepStage
import com.example.sleephony.ui.screen.report.viewmodel.SleepStageBlock
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SleepStageChart(
    blocks: List<SleepStageBlock>,
    sleepStartTime: LocalDateTime,
    sleepEndTime: LocalDateTime,
    modifier: Modifier = Modifier
) {
    val totalDuration = Duration.between(sleepStartTime, sleepEndTime).seconds.toFloat()
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Column(modifier = modifier.fillMaxWidth()) {
        SleepStage.values().forEach { stage ->
            val stageBlocks = blocks.filter { it.stage == stage }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 왼쪽 라벨
                Text(
                    text = stage.label,
                    color = stage.color,
                    modifier = Modifier.width(80.dp)
                )

                // 그래프 바
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                ) {
                    val totalWidthPx = constraints.maxWidth.toFloat()
                    val density = LocalDensity.current

                    stageBlocks.forEach { block ->
                        val startSec = Duration.between(sleepStartTime, block.start).seconds.toFloat()
                        val endSec = Duration.between(sleepStartTime, block.end).seconds.toFloat()

                        val startPx = totalWidthPx * (startSec / totalDuration)
                        val widthPx = totalWidthPx * ((endSec - startSec) / totalDuration)
                        val safeWidthPx = widthPx.coerceAtLeast(with(density) { 1.dp.toPx() })

                        with(density) {
                            Box(
                                modifier = Modifier
                                    .absoluteOffset(x = startPx.toDp())
                                    .width(safeWidthPx.toDp())
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(block.color)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // ✅ 하단 시간 표시
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 80.dp), // 라벨 공간만큼 padding
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = sleepStartTime.format(timeFormatter),
                color = Color.LightGray
            )
            Text(
                text = sleepEndTime.format(timeFormatter),
                color = Color.LightGray
            )
        }
    }
}
