package com.example.sleephony.utils

import com.example.sleephony.data.model.report.SleepDataResponse
import com.example.sleephony.ui.screen.report.viewmodel.SleepStage
import com.example.sleephony.ui.screen.report.viewmodel.SleepStageBlock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun List<SleepDataResponse>.toSleepStageBlocks(): List<SleepStageBlock> {
    if (this.isEmpty()) return emptyList()

    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val blocks = mutableListOf<SleepStageBlock>()

    var currentStage: SleepStage? = null
    var currentStart: LocalDateTime? = null

    for ((index, item) in this.withIndex()) {
        val time = LocalDateTime.parse(item.measuredAt, formatter)
        val stage = when (item.level) {
            "AWAKE" -> SleepStage.AWAKE
            "REM" -> SleepStage.REM
            "NREM1", "NREM2" -> SleepStage.LIGHT
            "NREM3" -> SleepStage.DEEP
            else -> SleepStage.LIGHT
        }

        if (currentStage == null) {
            currentStage = stage
            currentStart = time
        } else if (currentStage != stage) {
            val safeStart = requireNotNull(currentStart)
            blocks.add(SleepStageBlock(currentStage, safeStart, time, currentStage.color))
            currentStage = stage
            currentStart = time
        }

        // 마지막 블록 처리
        if (index == this.lastIndex) {
            val safeStart = requireNotNull(currentStart)
            val end = time.plusSeconds(30)
            blocks.add(SleepStageBlock(currentStage!!, safeStart, end, currentStage.color))
        }
    }

    return blocks
}
