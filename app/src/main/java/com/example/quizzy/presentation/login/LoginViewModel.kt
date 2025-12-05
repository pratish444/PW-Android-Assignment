package com.example.quizzy.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.data.model.AuthState
import com.example.quizzy.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _schoolId = MutableStateFlow("")
    val schoolId: StateFlow<String> = _schoolId.asStateFlow()

    private val _studentId = MutableStateFlow("")
    val studentId: StateFlow<String> = _studentId.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (authRepository.isUserAuthenticated()) {
            _authState.value = AuthState.Authenticated
        }
    }

    fun onSchoolIdChange(value: String) {
        _schoolId.value = value
    }

    fun onStudentIdChange(value: String) {
        _studentId.value = value
    }

    fun signIn() {
        if (_schoolId.value.isBlank() || _studentId.value.isBlank()) {
            _authState.value = AuthState.Error("Please enter both School ID and Student ID")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signInWithCustomToken(_schoolId.value, _studentId.value)

            _authState.value = if (result.isSuccess) {
                AuthState.Authenticated
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Sign in failed")
            }
        }
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }
}