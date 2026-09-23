package com.jpt.todotogether.core.domain.repository

import com.jpt.todotogether.core.domain.model.PrivacySettings

interface PrivacyConfigRepository {
    suspend fun getPrivacySettings(): PrivacySettings

    // replaces the settings wholesale (including the label list) and returns
    // what the backend saved.
    suspend fun updatePrivacySettings(settings: PrivacySettings): PrivacySettings
}
