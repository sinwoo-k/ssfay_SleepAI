package com.example.sleephony.ui.screen.auth

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephony.data.model.auth.SocialLoginResult
import com.example.sleephony.domain.repository.AuthRepository
import com.example.sleephony.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    /** 화면에서 관찰할 State */
    sealed interface UiState {
        object Idle : UiState
        object Loading : UiState
        object Authenticated   : UiState
        object NeedsProfile    : UiState
        data class Error(val message: String) : UiState
    }

    var uiState by mutableStateOf<UiState>(UiState.Idle)
        private set

    /** 카카오 로그인 연동 */
    fun signInWithKakao(activity: Activity) = viewModelScope.launch {
        uiState = UiState.Loading
        repo.loginWithKakao(activity)
            .onSuccess { result: SocialLoginResult ->
                if (result.status == "join") {
                    uiState = UiState.NeedsProfile
                } else {
                    userRepository.getUserProfile()
                    uiState = UiState.Authenticated
                }
            }
            .onFailure { e ->
                uiState = UiState.Error(e.message ?: "로그인 중 오류가 발생했습니다.")
            }
    }

    /** 구글 로그인 연동  */
    fun signInWithGoogle(activity: Activity) = viewModelScope.launch {
        uiState = UiState.Loading
        repo.loginWithGoogle(activity)
            .onSuccess { result: SocialLoginResult ->
                if (result.status == "join") {
                    uiState = UiState.NeedsProfile
                } else {
                    userRepository.getUserProfile()
                    uiState = UiState.Authenticated
                }
            }
            .onFailure { e ->
                uiState = UiState.Error(e.message ?: "로그인 중 오류가 발생했습니다.")
            }
    }

}
