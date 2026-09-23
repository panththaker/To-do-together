package com.jpt.todotogether.core.domain.repository

import com.jpt.todotogether.core.domain.model.FriendTodos

interface FriendTodoRepository {
    // throws ApiException with error "forbidden" if you aren't friends.
    suspend fun getFriendTodos(userId: Long): FriendTodos
}
