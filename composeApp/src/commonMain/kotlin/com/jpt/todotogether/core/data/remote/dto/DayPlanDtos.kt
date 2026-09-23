package com.jpt.todotogether.core.data.remote.dto

import kotlinx.serialization.Serializable

// mirrors the backend's dayPlanResponse (api/types.go).
@Serializable
data class DayPlanResponse(
    val plannedToday: Boolean,
)
