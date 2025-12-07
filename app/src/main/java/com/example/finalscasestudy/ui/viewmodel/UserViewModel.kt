package com.example.finalscasestudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalscasestudy.data.local.entities.User
import com.example.finalscasestudy.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    // Current logged-in user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    // Login and register results (true = success, false = error, null = no action)
    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult

    private val _registerResult = MutableStateFlow<String?>(null)
    val registerResult: StateFlow<String?> = _registerResult

    // REGISTER
    fun register(username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            // Input validation
            when {
                username.isBlank() -> _registerResult.value = "Username cannot be empty"
                email.isBlank() -> _registerResult.value = "Email cannot be empty"
                password.isBlank() -> _registerResult.value = "Password cannot be empty"
                password != confirmPassword -> _registerResult.value = "Passwords do not match"
                else -> {
                    val success = repository.registerUser(
                        User(username = username.trim(), email = email.trim(), password = password)
                    )
                    if (success) {
                        _currentUser.value = User(username = username.trim(), email = email.trim(), password = password)
                        _registerResult.value = "SUCCESS"
                    } else {
                        _registerResult.value = "User with this email already exists"
                    }
                }
            }
        }
    }

    // LOGIN
    fun login(email: String, password: String) {
        viewModelScope.launch {
            when {
                email.isBlank() -> _loginResult.value = "Email cannot be empty"
                password.isBlank() -> _loginResult.value = "Password cannot be empty"
                else -> {
                    val user = repository.loginUser(email.trim(), password)
                    if (user != null) {
                        _currentUser.value = user
                        _loginResult.value = "SUCCESS"
                    } else {
                        _loginResult.value = "Invalid email or password"
                    }
                }
            }
        }
    }

    // LOGOUT
    fun logout() {
        _currentUser.value = null
        _loginResult.value = null
        _registerResult.value = null
    }

    // RESET ERRORS (to be called when navigating to screen)
    fun resetErrors() {
        _loginResult.value = null
        _registerResult.value = null
    }
}
