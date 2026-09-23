package com.jpt.todotogether.core.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// field names mirror the backend's friendRequestDTO (api/types.go) so it
// decodes with no custom mapping. userId/username/name/avatarUrl describe the
// *other* user: the requester for incoming requests, the addressee for
// outgoing ones.
@OptIn(ExperimentalTime::class)
@Serializable
data class FriendRequest(
    val id: Long,
    val userId: Long,
    val username: String,
    val name: String,
    val avatarUrl: String,
    val createdAt: Instant,
)

enum class FriendRequestDirection {
    Incoming,
    Outgoing,
}
