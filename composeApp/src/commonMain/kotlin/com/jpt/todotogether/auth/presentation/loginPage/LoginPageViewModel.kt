package com.jpt.todotogether.auth.presentation.loginPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpt.todotogether.core.domain.repository.AuthRepository
import com.jpt.todotogether.core.domain.repository.SettingsRepository
import com.jpt.todotogether.home.presentation.homePage.HomePageAction
import com.jpt.todotogether.logger.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginPageViewModel(
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository,
    ) : ViewModel() {
    private val _state = MutableStateFlow(LoginPageState())
    val state = _state.asStateFlow()

    fun onAction(action: LoginPageAction) {
        when (action) {
            is LoginPageAction.OnLoginPressed -> {
                updateInitialSetupRan()
            }

            is LoginPageAction.OnApplePressed -> {
                updateInitialSetupRan()
            }

            is LoginPageAction.OnGoogleSignInSucceeded -> {
                completeGoogleSignIn(action.idToken)
            }

            is LoginPageAction.OnGoogleSignInFailed -> {
                _state.update { it.copy(isSigningIn = false, authError = action.message) }
            }

            is LoginPageAction.OnSignInPressed -> {
                updateInitialSetupRan()
            }
        }
    }

    fun updateInitialSetupRan() {
        viewModelScope.launch {
            runCatching {
                settingsRepository.updateSettings(
                    settingsRepository.getSettings().copy(
                        initialSetupRan = true
                    )
                )
            }.onFailure { error: Throwable ->
                AppLogger.error(
                    tag = "LoginPageViewModel",
                    message = "Error Updating Settings",
                    throwable = error
                )
            }
        }
    }

    private fun completeGoogleSignIn(idToken: String) = viewModelScope.launch {
        _state.update { it.copy(isSigningIn = true, authError = null) }
        runCatching { authRepository.signInWithGoogleIdToken(idToken) }
            .onSuccess { session ->
                _state.update { current ->
                    current.copy(
                        isSigningIn = false,
                        authUser = session.user,
                        authSession = session
                    )
                }
                // TODO: Maybe add nav here
            }
            .onFailure { throwable ->
                AppLogger.error(
                    tag = "LoginPageViewModel",
                    message = "Error completing Google sign-in",
                    throwable = throwable
                )
                _state.update { current ->
                    current.copy(
                        isSigningIn = false,
                        authError = throwable.message ?: "Sign-in failed"
                    )
                }
            }
    }
}