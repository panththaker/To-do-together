package com.jpt.todotogether.core.domain.repository

import com.jpt.todotogether.core.domain.model.AuthSession

interface AuthRepository {
    // returns null if the user isn't signed in; silently refreshes an expired
    // access token using the stored refresh token when possible.
    suspend fun getSession(): AuthSession?

    // exchanges a Google ID token (obtained client-side via KMPAuth's
    // rememberGoogleSignInState) for our own backend session.
    suspend fun signInWithGoogleIdToken(idToken: String): AuthSession

    suspend fun signOut()
}
