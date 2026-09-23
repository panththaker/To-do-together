package com.jpt.todotogether.core.data.repository

import com.jpt.todotogether.core.data.remote.TodoApiConfig
import com.jpt.todotogether.core.data.remote.bodyOrThrow
import com.jpt.todotogether.core.data.remote.dto.UpdateUsernameRequest
import com.jpt.todotogether.core.data.remote.dto.UserDto
import com.jpt.todotogether.core.data.remote.dto.toProfile
import com.jpt.todotogether.core.domain.model.Profile
import com.jpt.todotogether.core.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class KtorProfileRepository(
    private val client: HttpClient,
) : ProfileRepository {

    override suspend fun getProfile(): Profile =
        client.get("${TodoApiConfig.baseUrl}/auth/me").bodyOrThrow<UserDto>().toProfile()

    override suspend fun updateUsername(username: String): Profile =
        client.put("${TodoApiConfig.baseUrl}/me/username") {
            contentType(ContentType.Application.Json)
            setBody(UpdateUsernameRequest(username = username))
        }.bodyOrThrow<UserDto>().toProfile()
}
