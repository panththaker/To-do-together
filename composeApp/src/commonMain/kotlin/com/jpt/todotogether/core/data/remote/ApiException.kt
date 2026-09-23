package com.jpt.todotogether.core.data.remote

import com.jpt.todotogether.core.data.remote.dto.ErrorResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

// Thrown by repositories when the backend answers with a non-2xx status.
// `error` is the backend's machine-readable code (e.g. "not_found",
// "already_friends", "username_taken" - see writeJSONError calls in
// To-do-together-Backend/api) so callers can branch on it; `message` is the
// human-readable error_description.
class ApiException(
    val status: Int,
    val error: String,
    message: String,
) : Exception(message)

// Throws ApiException for non-2xx responses, decoding the backend's
// {"error", "error_description"} body when present.
suspend fun HttpResponse.ensureSuccess(): HttpResponse {
    if (status.isSuccess()) return this

    val error = runCatching { body<ErrorResponse>() }.getOrNull()
    throw ApiException(
        status = status.value,
        error = error?.error ?: "http_${status.value}",
        message = error?.error_description ?: status.description,
    )
}

suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T = ensureSuccess().body()
