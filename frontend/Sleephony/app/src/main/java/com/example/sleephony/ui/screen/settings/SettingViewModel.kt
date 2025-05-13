package com.example.sleephony.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.example.sleephony.data.datasource.local.UserLocalDataSource
import com.example.sleephony.data.model.user.UserProfileResult
import com.example.sleephony.utils.TokenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    userLocalDataSource: UserLocalDataSource,
    private val tokenProvider: TokenProvider
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
        }
    }

    fun deleteUserInfo() {

    }
}