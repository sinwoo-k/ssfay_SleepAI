package com.example.sleephony.data.datasource

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.example.sleephony.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.security.SecureRandom

class GoogleAuthDataSource(
    private val credentialManager: CredentialManager,
    private val context: Context,
    private val webClientId: String,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun signIn(activity: Activity): Result<String> = withContext(ioDispatcher) {
        runCatching {
            /* 1) 옵션 & 요청 생성 */
            val option = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(true)
                .build()



            val request = GetCredentialRequest.Builder()
                .addCredentialOption(option)
                .build()

            /* 2) Credential Manager 호출 (suspend) */
            val response = credentialManager.getCredential(
                context = activity,
                request = request
            )

            extractIdToken(response)
        }
    }

    /** GoogleIdTokenCredential 로부터 idToken 문자열만 추출 */
    private fun extractIdToken(resp: GetCredentialResponse): String {
        val cred = resp.credential as? CustomCredential
            ?: error("Credential is not CustomCredential")
        val googleCred = GoogleIdTokenCredential.createFrom(cred.data)

        Log.d("GOOGLE_ID_TOKEN", googleCred.idToken)      // 토큰 자체도 로그

        return googleCred.idToken
    }

    /** 재사용 공격 방지용 16바이트 난수 */
    private fun randomNonce(): String =
        ByteArray(16).also { SecureRandom().nextBytes(it) }
            .joinToString("") { "%02x".format(it) }
}
