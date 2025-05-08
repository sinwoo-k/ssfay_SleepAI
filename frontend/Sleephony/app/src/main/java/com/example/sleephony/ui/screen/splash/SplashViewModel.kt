package com.example.sleephony.ui.screen.splash

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephony.domain.repository.AuthRepository
import com.example.sleephony.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel(){
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn
    init {
        viewModelScope.launch {
            val loggedIn = authRepository.isLoggedIn()
            if (loggedIn) {
                userRepository.getUserProfile()
                _isLoggedIn.value = true
            } else {
                _isLoggedIn.value = false
            }
        }
    }

    // 알림 허용 여부 반환
    fun isNotificationEnabled(): Boolean {
        return NotificationManagerCompat
            .from(appContext)
            .areNotificationsEnabled()
    }

    fun openNotificationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            // Android O 이하용
            putExtra("app_uid", context.applicationInfo.uid)
        }
        context.startActivity(intent)
    }
}