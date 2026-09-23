package com.jpt.todotogether.core.domain.model

data class PrivacySettings(
    val visibility: TodoVisibility,
    val filterMode: TodoFilterMode,
    val labelFilters: List<String>
)
