package com.jpt.todotogether.core.domain.repository

import com.jpt.todotogether.core.domain.model.Todo
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface TodoRepository {
    suspend fun getTodos(): List<Todo>
    suspend fun createTodo(title: String, dueDate: Instant? = null, label: String? = null): Todo
    suspend fun updateTodo(todo: Todo): Boolean
    suspend fun deleteTodo(id: Int): Boolean
}