package com.example.sleephony.utils

import android.content.Context
import android.os.Environment
import java.io.File

object SoundFileHelper {
    private fun getMusicDir(context: Context, themeId: Int): File {
        val parent = File(
            context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "theme_$themeId"
        )
        if (!parent.exists()) parent.mkdirs()
        return parent
    }

    fun getSoundFile(context: Context, themeId: Int, soundId: Int): File {
        return File(getMusicDir(context, themeId), "sound_$soundId.mp3")
    }

    fun isDownloaded(context: Context, themeId: Int, soundId: Int): Boolean {
        return getSoundFile(context, themeId, soundId).exists()
    }

    fun isAllDownloaded(context: Context, themeId: Int): Boolean {
        val soundDir = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "theme_${themeId}")
        if (!soundDir.exists()) return false
        val expectedCount = 4  // 예: 사운드 4개일 경우, 서버 응답으로 대체해도 됨
        return soundDir.listFiles()?.size == expectedCount
    }
}