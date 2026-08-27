package com.jpt.todotogether.core.domain.repository

import com.jpt.todotogether.core.domain.model.Todo

interface TodoRepository {
    suspend fun getTodos(): List<Todo>
    suspend fun createTodo(title: String): Todo
    suspend fun updateTodo(todo: Todo): Boolean
    suspend fun deleteTodo(id: Int): Boolean
}