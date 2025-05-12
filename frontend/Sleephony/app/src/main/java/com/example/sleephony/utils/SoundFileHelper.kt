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
}