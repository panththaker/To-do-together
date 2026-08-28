package com.jpt.todotogether.core.data.remote.dto

import kotlinx.serialization.Serializable

// Wire types matching the backend's snake_case token JSON (see
// To-do-together-Backend/api/types.go) - distinct from the Todo API's
// camelCase, which is a separate convention on that side. Shared between
// KtorAuthRepository (explicit sign-in/out) and the HttpClient's bearer Auth
// provider (transparent attach + refresh-on-401), which both need to hit
// POST /auth/token.
@Serializable
data class TokenRequest(
    val grant_type: String,
    val id_token: String? = null,
    val refresh_token: String? = null,
)

@Serializable
data class UserDto(
    val id: Long,
    val email: String,
    val name: String,
    val avatarUrl: String,
)

@Serializable
data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String,
    val expires_in: Long,
    val user: UserDto,
)
