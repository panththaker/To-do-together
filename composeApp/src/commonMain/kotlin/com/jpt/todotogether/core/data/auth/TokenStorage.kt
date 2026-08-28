package com.jpt.todotogether.core.data.auth

import com.jpt.todotogether.AppDatabase
import com.jpt.todotogether.core.data.remote.dto.TokenResponse
import com.jpt.todotogether.core.domain.model.AuthSession
import com.jpt.todotogether.core.domain.model.AuthUser
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// Single source of truth for the persisted access/refresh token pair. Shared
// between KtorAuthRepository (explicit sign-in/out) and the shared
// HttpClient's bearer Auth provider (transparent attach + refresh-on-401, see
// Modules.kt) so both sides read and write the same row.
@OptIn(ExperimentalTime::class)
class TokenStorage(private val db: AppDatabase) {

    fun getSession(): AuthSession? {
        val row = db.appDatabaseQueries.getAuthTokens().executeAsOneOrNull() ?: return null
        return AuthSession(
            accessToken = row.access_token,
            refreshToken = row.refresh_token,
            expiresAtEpochMillis = row.expires_at_epoch_millis,
            user = AuthUser(row.user_id, row.user_email, row.user_name, row.user_avatar_url),
        )
    }

    fun save(response: TokenResponse): AuthSession {
        val expiresAt = Clock.System.now().toEpochMilliseconds() + response.expires_in * 1000
        val user = AuthUser(response.user.id, response.user.email, response.user.name, response.user.avatarUrl)

        db.appDatabaseQueries.saveAuthTokens(
            access_token = response.access_token,
            refresh_token = response.refresh_token,
            expires_at_epoch_millis = expiresAt,
            user_id = user.id,
            user_email = user.email,
            user_name = user.name,
            user_avatar_url = user.avatarUrl,
        )

        return AuthSession(response.access_token, response.refresh_token, expiresAt, user)
    }

    fun clear() {
        db.appDatabaseQueries.clearAuthTokens()
    }
}
