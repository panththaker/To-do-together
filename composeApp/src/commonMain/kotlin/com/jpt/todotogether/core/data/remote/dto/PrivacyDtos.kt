package com.jpt.todotogether.core.data.remote.dto

import com.jpt.todotogether.core.domain.model.PrivacySettings
import com.jpt.todotogether.core.domain.model.TodoFilterMode
import com.jpt.todotogether.core.domain.model.TodoVisibility
import kotlinx.serialization.Serializable

// mirrors the backend's privacySettingsDTO / updatePrivacyRequest (same
// shape, api/types.go) - field names differ from the domain PrivacySettings.
@Serializable
data class PrivacySettingsDto(
    val visibility: TodoVisibility,
    val labelFilterMode: TodoFilterMode,
    val labels: List<String> = emptyList(),
)

fun PrivacySettingsDto.toDomain() = PrivacySettings(
    visibility = visibility,
    filterMode = labelFilterMode,
    labelFilters = labels,
)

fun PrivacySettings.toDto() = PrivacySettingsDto(
    visibility = visibility,
    labelFilterMode = filterMode,
    labels = labelFilters,
)
