package com.jpt.todotogether.home.presentation.homePage

import com.jpt.todotogether.core.domain.model.AuthSession
import com.jpt.todotogether.core.domain.model.AuthUser
import com.jpt.todotogether.core.domain.model.Todo
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// the state that is provided to the screen composable,
// should only contain data that is relevant to the ui, and should be immutable.
@OptIn(ExperimentalTime::class)
data class HomePageState(
    val isLoading: Boolean = false,
    val count: Long = 0,
    val todos: List<Todo> = emptyList(),
    val newTodoTitle: String = "",
    val newTodoDueDate: Instant? = null,
    val newTodoLabel: String = "",
    val authUser: AuthUser? = null,
    val isSigningIn: Boolean = false,
    val authError: String? = null,
    // full session (tokens + expiry), kept only for the auth debug panel -
    // authUser above is what normal UI should read.
    val authSession: AuthSession? = null,
    // true while the startup session check (silent reauth/refresh) is in flight.
    val isSessionLoading: Boolean = true,
)
