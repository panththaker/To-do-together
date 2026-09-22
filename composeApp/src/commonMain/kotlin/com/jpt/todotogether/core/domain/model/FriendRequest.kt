package com.jpt.todotogether.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FriendRequest(
    val username: String,
)