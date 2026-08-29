package com.jpt.todotogether.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthUser(
    val id: Long,
    val email: String,
    val name: String,
    val avatarUrl: String,
)
