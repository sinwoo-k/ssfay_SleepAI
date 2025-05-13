package com.example.sleephony.ui.screen.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephony.data.datasource.local.UserLocalDataSource
import com.example.sleephony.data.model.user.UserProfileRequest
import com.example.sleephony.data.model.user.UserProfileResult
import com.example.sleephony.domain.repository.UserRepository
import com.example.sleephony.navigation.ProfileStep
import com.example.sleephony.utils.TokenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository
) : ViewModel(){

    companion object {
        private val DEFAULT_PROFILE = UserProfileResult(
            email     = "",
            nickname  = "",
            birthDate = "",
            gender    = "",
            height    = 0,
            weight    = 0
        )
    }


    private val _profileState = MutableStateFlow(DEFAULT_PROFILE)
    val profileState: StateFlow<UserProfileResult> = _profileState

    init {
        _profileState.value = userLocalDataSource.getProfile()
    }

    fun logout () {
        val token  = tokenProvider.getToken()
        if (token != null)  {
            tokenProvider.clearToken(token)
            userLocalDataSource.clearProfile()
        }
    }


    fun isValid(key: String, value: String): Boolean {
        return when(key) {
            "birthDate" ->
                Regex("""\d{4}\.\d{2}\.\d{2}""").matches(value)
            "gender" ->
                value in listOf("남자","여자")
            "height" ->
                value.toIntOrNull()?.let { it in 100..250 } ?: false
            "weight" ->
                value.toIntOrNull()?.let { it in 20..200 } ?: false
            "nickname" ->
                value.isNotBlank() && value.length <= 10
            else -> false
        }
    }

    fun patchUserProfile(key: String, value: String) {
        viewModelScope.launch {

            userLocalDataSource.updateProfileField(key, value)

            val updated = userLocalDataSource.getProfile()

            _profileState.value = userLocalDataSource.getProfile()

            val req = UserProfileRequest(
                nickname = updated.nickname,
                height = updated.height,
                weight = updated.weight,
                birthDate = updated.birthDate,
                gender = updated.gender
            )

            userRepository.patchUserProfile(req)
                .onSuccess {
                    _profileState.value = updated
                }
                .onFailure { err ->
                    Log.e("DBG", "프로필 수정 실패", err)
                }
        }
    }

    fun deleteUserInfo() {
        viewModelScope.launch {
            val response  = userRepository.deleteUser()
            Log.d("DBG", "$response")
            logout()
        }
    }
}