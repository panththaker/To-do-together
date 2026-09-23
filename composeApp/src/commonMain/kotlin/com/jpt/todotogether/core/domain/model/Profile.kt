package com.jpt.todotogether.core.domain.model

// the signed-in user's full account, as returned by GET /auth/me and
// PUT /me/username. Kept separate from AuthUser (which is what TokenStorage
// persists alongside the tokens) so adding username didn't require a
// SQLDelight schema migration.
data class Profile(
    val id: Long,
    val email: String,
    val name: String,
    val avatarUrl: String,
    val username: String,
)
