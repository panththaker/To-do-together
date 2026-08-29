package com.jpt.todotogether.core.data.repository

import com.jpt.todotogether.core.data.auth.TokenStorage
import com.jpt.todotogether.core.data.remote.TodoApiConfig
import com.jpt.todotogether.core.data.remote.dto.TokenRequest
import com.jpt.todotogether.core.data.remote.dto.TokenResponse
import com.jpt.todotogether.core.domain.model.AuthSession
import com.jpt.todotogether.core.domain.repository.AuthRepository
import com.mmk.kmpauth.google.GoogleAuthProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class KtorAuthRepository(
    private val client: HttpClient,
    private val tokenStorage: TokenStorage,
) : AuthRepository {

    override suspend fun getSession(): AuthSession? {
        val session = tokenStorage.getSession() ?: return null

        if (Clock.System.now().toEpochMilliseconds() < session.expiresAtEpochMillis) return session

        return runCatching { refresh(session.refreshToken) }.getOrElse {
            signOut()
            null
        }
    }

    override suspend fun signInWithGoogleIdToken(idToken: String): AuthSession =
        exchangeToken(TokenRequest(grant_type = "google_id_token", id_token = idToken))

    override suspend fun signOut() {
        tokenStorage.clear()
        // Clears Credential Manager's remembered account so the next
        // sign-in shows the picker instead of auto-selecting.
        runCatching { GoogleAuthProvider.get().signOut() }
    }

    private suspend fun refresh(refreshToken: String): AuthSession =
        exchangeToken(TokenRequest(grant_type = "refresh_token", refresh_token = refreshToken))

    private suspend fun exchangeToken(request: TokenRequest): AuthSession {
        val response: TokenResponse = client.post("${TodoApiConfig.baseUrl}/auth/token") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

        return tokenStorage.save(response)
    }
}
