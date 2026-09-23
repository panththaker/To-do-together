package com.jpt.todotogether.core.data.remote.dto

import kotlinx.serialization.Serializable

// mirrors the backend's errorResponse (api/types.go), returned by every
// endpoint on a non-2xx status. Snake_case like the token types.
@Serializable
data class ErrorResponse(
    val error: String,
    val error_description: String = "",
)
