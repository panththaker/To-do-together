package com.jpt.todotogether.core.data.repository

import com.jpt.todotogether.core.data.remote.TodoApiConfig
import com.jpt.todotogether.core.data.remote.bodyOrThrow
import com.jpt.todotogether.core.data.remote.dto.PrivacySettingsDto
import com.jpt.todotogether.core.data.remote.dto.toDomain
import com.jpt.todotogether.core.data.remote.dto.toDto
import com.jpt.todotogether.core.domain.model.PrivacySettings
import com.jpt.todotogether.core.domain.repository.PrivacyConfigRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class KtorPrivacyConfigRepository(
    private val client: HttpClient,
) : PrivacyConfigRepository {

    override suspend fun getPrivacySettings(): PrivacySettings =
        client.get("${TodoApiConfig.baseUrl}/me/privacy").bodyOrThrow<PrivacySettingsDto>().toDomain()

    override suspend fun updatePrivacySettings(settings: PrivacySettings): PrivacySettings =
        client.put("${TodoApiConfig.baseUrl}/me/privacy") {
            contentType(ContentType.Application.Json)
            setBody(settings.toDto())
        }.bodyOrThrow<PrivacySettingsDto>().toDomain()
}
