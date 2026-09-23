package com.jpt.todotogether.core.domain.repository

import com.jpt.todotogether.core.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(): Profile

    // username must be 3-20 chars of [a-z0-9_]; throws ApiException with
    // error "invalid_username" or "username_taken" otherwise.
    suspend fun updateUsername(username: String): Profile
}
