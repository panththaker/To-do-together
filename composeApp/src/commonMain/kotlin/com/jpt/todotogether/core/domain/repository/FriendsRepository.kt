package com.jpt.todotogether.core.domain.repository

import com.jpt.todotogether.core.domain.model.FriendRequest
import com.jpt.todotogether.core.domain.model.User

interface FriendsRepository {
    suspend fun sendFriendRequest(username: String)
    suspend fun listFriendRequests(): List<FriendRequest>?
    suspend fun acceptFriendRequest(username: String)
    suspend fun rejectFriendRequest(username: String)
    suspend fun listFriends(): List<User>?
    suspend fun unfriend(username: String)
}