package com.jpt.todotogether.auth.presentation.loginPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpt.todotogether.core.domain.repository.SettingsRepository
import com.jpt.todotogether.logger.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginPageViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginPageState())
    val state = _state.asStateFlow()

    fun onAction(action: LoginPageAction){
        when (action){
            is LoginPageAction.OnLoginPressed -> {
                updateInitialSetupRan()
            }
            is LoginPageAction.OnApplePressed -> {
                updateInitialSetupRan()
            }
            is LoginPageAction.OnGooglePressed -> {
                updateInitialSetupRan()
            }
            is LoginPageAction.OnSignInPressed -> {
                updateInitialSetupRan()
            }
        }
    }

    fun updateInitialSetupRan(){
        viewModelScope.launch {
            runCatching {
                settingsRepository.updateSettings(
                    settingsRepository.getSettings().copy(
                        initialSetupRan = true
                    )
                )
            }.onFailure {
                    error: Throwable ->
                AppLogger.error(
                    tag = "LoginPageViewModel",
                    message = "Error Updating Settings",
                    throwable = error
                )
            }
        }
    }
}