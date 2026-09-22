package com.jpt.todotogether.core.domain.repository

import com.jpt.todotogether.core.domain.model.Todo

interface FriendTodoRepository {
    suspend fun getFriendTodos(userID: Int): List<Todo>
}