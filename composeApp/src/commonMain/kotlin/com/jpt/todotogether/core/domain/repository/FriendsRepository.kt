package com.jpt.todotogether.core.domain.repository

import com.jpt.todotogether.core.domain.model.FriendRequest
import com.jpt.todotogether.core.domain.model.FriendRequestDirection
import com.jpt.todotogether.core.domain.model.User

// all calls throw ApiException on a backend error, e.g. "not_found",
// "already_friends" or "request_pending" from sendFriendRequest.
interface FriendsRepository {
    // if the target has already requested you, this accepts their request
    // instead of creating a new one.
    suspend fun sendFriendRequest(username: String)
    suspend fun listFriendRequests(direction: FriendRequestDirection = FriendRequestDirection.Incoming): List<FriendRequest>
    suspend fun acceptFriendRequest(requestId: Long)
    suspend fun declineFriendRequest(requestId: Long)
    suspend fun listFriends(): List<User>
    suspend fun unfriend(userId: Long)
}
