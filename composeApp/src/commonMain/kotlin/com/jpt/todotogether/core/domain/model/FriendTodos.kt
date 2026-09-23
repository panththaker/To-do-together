package com.jpt.todotogether.core.domain.model

// what a friend's privacy settings let us see of their todos - one case per
// TodoVisibility (see the backend's FriendTodos handler, api/friend_todos.go).
sealed interface FriendTodos {
    data class All(val todos: List<Todo>) : FriendTodos
    data class Count(val count: Long) : FriendTodos
    data class ScheduledStatus(val plannedToday: Boolean) : FriendTodos
}
