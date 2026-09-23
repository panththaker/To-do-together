package com.jpt.todotogether.core.domain.model

import kotlinx.serialization.Serializable

// mirrors the backend's friendDTO (api/types.go).
@Serializable
data class User(
    val id: Long,
    val username: String,
    val name: String,
    val avatarUrl: String,
)
