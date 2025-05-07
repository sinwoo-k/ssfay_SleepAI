package com.example.sleephony.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephony.domain.repository.AuthRepository
import com.example.sleephony.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
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
}