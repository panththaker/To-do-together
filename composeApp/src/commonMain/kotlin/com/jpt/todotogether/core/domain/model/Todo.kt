package com.jpt.todotogether.core.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class Todo(
    val id: Int,
    val title: String,
    val completed: Boolean,
    val createdAt: Instant,
    val dueDate: Instant?,
    val completedAt: Instant?,
    val label: String?,
)