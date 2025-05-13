package com.example.sleephony.utils

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null

    /** themeId, soundId 에 해당하는 로컬 파일을 재생 */
    fun play(themeId: Int, soundId: Int) {
        val file = SoundFileHelper.getSoundFile(context, themeId, soundId)
        if (!file.exists()) return

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            prepare()
            start()
        }
    }

    /** 재생 중지 & 리소스 해제 */
    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
