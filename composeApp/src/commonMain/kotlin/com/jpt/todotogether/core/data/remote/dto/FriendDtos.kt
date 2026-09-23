package com.jpt.todotogether.core.data.remote.dto

import com.jpt.todotogether.core.domain.model.FriendTodos
import com.jpt.todotogether.core.domain.model.Todo
import com.jpt.todotogether.core.domain.model.TodoVisibility
import kotlinx.serialization.Serializable

// mirrors the backend's friendRequestRequest (api/types.go). Friend and
// friend-request responses decode straight into the domain User and
// FriendRequest, whose fields match friendDTO / friendRequestDTO.
@Serializable
data class SendFriendRequestRequest(
    val username: String,
)

// mirrors the backend's friendTodosResponse (api/types.go). Only the field
// matching `visibility` is populated; the others are omitted. `todos` is also
// omitted when empty (Go's omitempty), hence the default.
@Serializable
data class FriendTodosResponse(
    val visibility: TodoVisibility,
    val todos: List<Todo> = emptyList(),
    val count: Long? = null,
    val plannedToday: Boolean? = null,
)

fun FriendTodosResponse.toDomain(): FriendTodos = when (visibility) {
    TodoVisibility.All -> FriendTodos.All(todos)
    TodoVisibility.Count -> FriendTodos.Count(count ?: 0)
    TodoVisibility.ScheduledStatus -> FriendTodos.ScheduledStatus(plannedToday ?: false)
}
