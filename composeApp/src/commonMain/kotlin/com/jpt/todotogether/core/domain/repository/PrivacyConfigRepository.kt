package com.jpt.todotogether.core.domain.repository

import com.jpt.todotogether.core.domain.model.PrivacySettings

interface PrivacyConfigRepository {
    suspend fun getPrivacySettings(): PrivacySettings
    suspend fun updatePrivacySettings(settings: PrivacySettings): Boolean
}