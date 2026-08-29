package com.jpt.todotogether.auth.presentation.welcomePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpt.todotogether.core.domain.model.Settings
import com.jpt.todotogether.core.domain.repository.SettingsRepository
import com.jpt.todotogether.logger.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WelcomePageViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    // _state is the mutable version of our state
    private val _state = MutableStateFlow(WelcomePageState())

    // this is the state we actually pass around, must be immutable
    val state = _state.asStateFlow()

    fun onAction(action: WelcomePageAction){
        when (action){
            is WelcomePageAction.OnGetStartedPressed -> {
                updateInitialSetupRan()
            }
            is WelcomePageAction.OnLoginPressed -> {
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
            }.onFailure {
                error: Throwable ->
                AppLogger.error(
                    tag = "WelcomePageViewModel",
                    message = "Error Updating Settings",
                    throwable = error
                )
            }
        }
    }
}