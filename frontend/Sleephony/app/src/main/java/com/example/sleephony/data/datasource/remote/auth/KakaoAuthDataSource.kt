package com.example.sleephony.data.datasource.remote.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.auth.model.OAuthToken
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class KakaoAuthDataSource {
    /** 카카오톡 또는 계정으로 로그인 → OAuthToken 반환 */
    suspend fun login(activity: Activity): OAuthToken = suspendCancellableCoroutine { cont ->
        val cb: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            Log.d("DBG","token=$token error=$error")
            when {
                token != null -> cont.resume(token)
                else          -> cont.resumeWithException(error ?: Exception("Unknown error"))
            }
        }

        val api = UserApiClient.instance
        if (api.isKakaoTalkLoginAvailable(activity))
            api.loginWithKakaoTalk(activity, callback = cb)
        else
            api.loginWithKakaoAccount(activity, callback = cb)
    }
}
