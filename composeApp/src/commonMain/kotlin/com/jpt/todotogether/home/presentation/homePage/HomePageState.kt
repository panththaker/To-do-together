package com.jpt.todotogether.home.presentation.homePage

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
)
