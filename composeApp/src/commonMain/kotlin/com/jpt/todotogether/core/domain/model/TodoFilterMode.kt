package com.jpt.todotogether.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// serial names match the backend's validLabelFilterModes (api/privacy.go).
@Serializable
enum class TodoFilterMode {
    @SerialName("none") None,
    @SerialName("whitelist") Whitelist,
    @SerialName("blacklist") Blacklist,
}
