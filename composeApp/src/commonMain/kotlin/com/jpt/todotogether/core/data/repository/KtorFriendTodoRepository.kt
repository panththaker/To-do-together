package com.jpt.todotogether.core.data.repository

import com.jpt.todotogether.core.data.remote.TodoApiConfig
import com.jpt.todotogether.core.data.remote.bodyOrThrow
import com.jpt.todotogether.core.data.remote.dto.FriendTodosResponse
import com.jpt.todotogether.core.data.remote.dto.toDomain
import com.jpt.todotogether.core.domain.model.FriendTodos
import com.jpt.todotogether.core.domain.repository.FriendTodoRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorFriendTodoRepository(
    private val client: HttpClient,
) : FriendTodoRepository {

    override suspend fun getFriendTodos(userId: Long): FriendTodos =
        client.get("${TodoApiConfig.baseUrl}/friends/$userId/todos").bodyOrThrow<FriendTodosResponse>().toDomain()
}
