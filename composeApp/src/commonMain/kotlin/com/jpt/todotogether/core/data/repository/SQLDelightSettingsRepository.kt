package com.jpt.todotogether.core.data.repository

import com.jpt.todotogether.AppDatabase
import com.jpt.todotogether.core.domain.model.Settings
import com.jpt.todotogether.core.domain.repository.SettingsRepository

class SQLDelightSettingsRepository(
    private val db: AppDatabase,
) : SettingsRepository {
    override suspend fun getSettings(): Settings {
        val settingsFromDb = db.appDatabaseQueries.getSettings().executeAsOneOrNull()
        
        // If settings don't exist, initialize with defaults
        if (settingsFromDb == null) {
            val defaultSettings = Settings()
            updateSettings(defaultSettings)
            return defaultSettings
        }
        
        return Settings(
            initialSetupRan = settingsFromDb.initial_setup_ran == 1L,
            theme = settingsFromDb.theme
        )
    }

    override suspend fun updateSettings(settings: Settings) {
        db.appDatabaseQueries.insertOrUpdateSettings(
            initial_setup_ran = if (settings.initialSetupRan) 1L else 0L,
            theme = settings.theme,
            counter = settings.counter
        )
    }
}
