package com.jpt.todotogether.core.data.remote.dto

import com.jpt.todotogether.core.domain.model.Profile
import kotlinx.serialization.Serializable

// mirrors the backend's usernameRequest (api/types.go). GET /auth/me and
// PUT /me/username both respond with the userDTO shape, i.e. UserDto in
// TokenDtos.kt.
@Serializable
data class UpdateUsernameRequest(
    val username: String,
)

fun UserDto.toProfile() = Profile(
    id = id,
    email = email,
    name = name,
    avatarUrl = avatar_url,
    username = username,
)
