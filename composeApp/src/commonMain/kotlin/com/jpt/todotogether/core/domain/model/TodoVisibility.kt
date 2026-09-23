package com.jpt.todotogether.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// serial names match the backend's validVisibilities (api/privacy.go).
@Serializable
enum class TodoVisibility {
    @SerialName("all") All,
    @SerialName("count") Count,
    @SerialName("scheduled_status") ScheduledStatus,
}
