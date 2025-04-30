package com.example.sleephony.ui.screen.auth

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephony.domain.repository.AuthRepository
import com.google.gson.Gson
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    /** 화면에서 관찰할 State */
    sealed interface UiState {
        object Idle : UiState
        object Loading : UiState
        data class KakaoSuccess(val token: OAuthToken) : UiState
        data class Error(val throwable: Throwable) : UiState
    }

    var uiState by mutableStateOf<UiState>(UiState.Idle)
        private set

    /* ───────── 카카오 로그인 ───────── */
    fun signInWithKakao(activity: Activity) = viewModelScope.launch {
        Log.d("DBG","VM start")
        val gson = Gson()
        uiState = UiState.Loading
        repo.loginWithKakao(activity)
            .onSuccess {
                Log.d("TAG", "check ${gson.toJson(it)}")
                uiState = UiState.KakaoSuccess(it)
            }
            .onFailure { uiState = UiState.Error(it) }
    }

}
