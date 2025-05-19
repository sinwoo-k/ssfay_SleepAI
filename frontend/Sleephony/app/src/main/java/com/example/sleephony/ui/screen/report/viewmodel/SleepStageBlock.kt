package com.example.sleephony.ui.screen.report.viewmodel

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime

enum class SleepStage(val label: String, val color: Color) {
    AWAKE("비수면", Color(0xFFE1C16E)),
    REM("램수면", Color(0xFFB388FF)),
    LIGHT("얕은잠", Color(0xFF90CAF9)),
    DEEP("깊은잠", Color(0xFF0077B6))
}

data class SleepStageBlock(
    val stage: SleepStage,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val color: Color
)

enum class SleepCategory(val label: String) {
    TOTAL("총 수면시간"),
    DEEP("깊은잠"),
    REM("램수면")
}

