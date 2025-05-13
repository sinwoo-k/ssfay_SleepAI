package com.example.sleephony.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleephony.data.model.user.UserProfileRequest
import com.example.sleephony.domain.repository.AuthRepository
import com.example.sleephony.domain.repository.UserRepository
import com.example.sleephony.navigation.ProfileStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val userRepository: UserRepository
): ViewModel(){
    private val _values = MutableStateFlow<Map<ProfileStep, String>>(emptyMap())
    val stepValues: StateFlow<Map<ProfileStep, String>> = _values

    private val _submitState = MutableStateFlow<Result<Unit>?>(null)
    val submitState: StateFlow<Result<Unit>?> = _submitState

    fun updateValue(step: ProfileStep, value: String) {
        _values.value += (step to value)
    }

    fun isValid(step: ProfileStep): Boolean {
        val v = _values.value[step] ?: return false
        return when(step) {
            ProfileStep.BIRTHDAY ->
                Regex("""\d{4}\.\d{2}\.\d{2}""").matches(v)
            ProfileStep.GENDER ->
                v in listOf("남자","여자")
            ProfileStep.HEIGHT ->
                v.toIntOrNull()?.let { it in 100..250 } ?: false
            ProfileStep.WEIGHT ->
                v.toIntOrNull()?.let { it in 20..200 } ?: false
            ProfileStep.NICKNAME ->
                v.isNotBlank() && v.length <= 10
        }
    }

    fun submitProfile() {
        viewModelScope.launch {
            val data = getAllProfileData()
            // 필수 값이 없으면 early return 혹은 error state
            val nickname = data[ProfileStep.NICKNAME]?.trim()
                ?: return@launch /* 또는 throw IllegalStateException("닉네임이 필요합니다") */

            val height = data[ProfileStep.HEIGHT]?.toIntOrNull()
                ?: return@launch /* 또는 throw */

            val weight = data[ProfileStep.WEIGHT]?.toIntOrNull()
                ?: return@launch

            val birth = data[ProfileStep.BIRTHDAY]?.replace('.', '-') ?: ""
            val genderCode = when(data[ProfileStep.GENDER]) {
                "남자" -> "M"
                "여자" -> "F"
                else    -> "M"
            }

            val req = UserProfileRequest(
                nickname  = nickname,
                height    = height,
                weight    = weight,
                birthDate = birth,
                gender    = genderCode
            )

            repo.submitProfile(req)
                .onSuccess {
                    userRepository.getUserProfile()
                    _submitState.value = Result.success(Unit)
                }
                .onFailure { e ->
                    _submitState.value = Result.failure(e)
                }
        }
    }

    private fun getAllProfileData(): Map<ProfileStep, String> = _values.value
}