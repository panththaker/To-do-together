package com.jpt.todotogether.core.data.remote.dto

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// mirrors the backend's createTodoRequest (api/todos.go). Responses decode
// straight into the domain Todo, whose fields match todoDTO exactly.
@OptIn(ExperimentalTime::class)
@Serializable
data class CreateTodoRequest(
    val title: String,
    val dueDate: Instant? = null,
    val label: String? = null,
)
