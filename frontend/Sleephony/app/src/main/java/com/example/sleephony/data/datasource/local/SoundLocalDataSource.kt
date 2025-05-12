package com.example.sleephony.data.datasource.local

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.example.sleephony.data.model.theme.SoundDto
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundLocalDataSource @Inject constructor(@ApplicationContext private val context: Context) {

    private val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    fun enqueueDownload(themeId: Int, sound: SoundDto): Long {
        val url = sound.soundUrl.trim()
        // 1) 다운로드 매니저 요청 생성
        val request = DownloadManager.Request(url.toUri()).apply {
            setTitle(sound.soundName)
            setDescription("테마 $themeId 사운드 내려받기")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            // 2) 내부 저장소 대신 앱 전용 외부 저장소로 목적지 지정
            //    Environment.DIRECTORY_MUSIC 하위에 "theme_<id>/sound_<id>.mp3" 경로로 저장
            setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_MUSIC,
                "theme_${themeId}/sound_${sound.soundId}.mp3"
            )
            // 3) 네트워크 타입 허용
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        }

        return dm.enqueue(request)
    }
}