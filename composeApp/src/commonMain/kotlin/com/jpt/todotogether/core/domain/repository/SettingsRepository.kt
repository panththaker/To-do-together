package com.jpt.todotogether.core.domain.repository

import com.jpt.todotogether.core.domain.model.Settings

interface SettingsRepository {
    suspend fun getSettings(): Settings
    suspend fun updateSettings(settings: Settings)
}
