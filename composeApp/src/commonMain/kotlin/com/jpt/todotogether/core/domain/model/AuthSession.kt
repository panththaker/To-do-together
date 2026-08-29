package com.jpt.todotogether.core.domain.model

data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
    val expiresAtEpochMillis: Long,
    val user: AuthUser,
)
