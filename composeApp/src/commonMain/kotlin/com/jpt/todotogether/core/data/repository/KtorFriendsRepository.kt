package com.jpt.todotogether.core.data.repository

import com.jpt.todotogether.core.data.remote.TodoApiConfig
import com.jpt.todotogether.core.data.remote.bodyOrThrow
import com.jpt.todotogether.core.data.remote.dto.SendFriendRequestRequest
import com.jpt.todotogether.core.data.remote.ensureSuccess
import com.jpt.todotogether.core.domain.model.FriendRequest
import com.jpt.todotogether.core.domain.model.FriendRequestDirection
import com.jpt.todotogether.core.domain.model.User
import com.jpt.todotogether.core.domain.repository.FriendsRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class KtorFriendsRepository(
    private val client: HttpClient,
) : FriendsRepository {

    override suspend fun sendFriendRequest(username: String) {
        client.post("${TodoApiConfig.baseUrl}/friends/requests") {
            contentType(ContentType.Application.Json)
            setBody(SendFriendRequestRequest(username = username))
        }.ensureSuccess()
    }

    override suspend fun listFriendRequests(direction: FriendRequestDirection): List<FriendRequest> =
        client.get("${TodoApiConfig.baseUrl}/friends/requests") {
            parameter("direction", direction.name.lowercase())
        }.bodyOrThrow()

    override suspend fun acceptFriendRequest(requestId: Long) {
        client.post("${TodoApiConfig.baseUrl}/friends/requests/$requestId/accept").ensureSuccess()
    }

    override suspend fun declineFriendRequest(requestId: Long) {
        client.post("${TodoApiConfig.baseUrl}/friends/requests/$requestId/decline").ensureSuccess()
    }

    override suspend fun listFriends(): List<User> =
        client.get("${TodoApiConfig.baseUrl}/friends").bodyOrThrow()

    override suspend fun unfriend(userId: Long) {
        client.delete("${TodoApiConfig.baseUrl}/friends/$userId").ensureSuccess()
    }
}
