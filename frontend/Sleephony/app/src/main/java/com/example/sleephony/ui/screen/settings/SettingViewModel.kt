package com.example.sleephony.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.example.sleephony.data.datasource.local.UserLocalDataSource
import com.example.sleephony.data.model.user.UserProfileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource
) : ViewModel(){
    private val _profileState = MutableStateFlow<UserProfileResult?>(null)
    val profileState: StateFlow<UserProfileResult?> = _profileState

    init {
        _profileState.value = userLocalDataSource.getProfile()
    }
}