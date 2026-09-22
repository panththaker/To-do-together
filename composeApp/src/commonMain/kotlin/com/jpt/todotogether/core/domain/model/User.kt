package com.jpt.todotogether.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val name: String,
    val avatarUrl: String,
)
