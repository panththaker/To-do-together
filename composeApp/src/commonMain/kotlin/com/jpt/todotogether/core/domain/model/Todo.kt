package com.jpt.todotogether.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val id: Int,
    val title: String,
    val completed: Boolean,
    val createdAt: Long,
    val dueDate: Long?,
    val completedAt: Long?,
)