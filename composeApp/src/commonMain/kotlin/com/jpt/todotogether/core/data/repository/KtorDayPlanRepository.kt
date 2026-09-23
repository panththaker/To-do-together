package com.jpt.todotogether.core.data.repository

import com.jpt.todotogether.core.data.remote.TodoApiConfig
import com.jpt.todotogether.core.data.remote.bodyOrThrow
import com.jpt.todotogether.core.data.remote.dto.DayPlanResponse
import com.jpt.todotogether.core.domain.repository.DayPlanRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post

class KtorDayPlanRepository(
    private val client: HttpClient,
) : DayPlanRepository {

    override suspend fun markDayPlanned(): Boolean =
        client.post("${TodoApiConfig.baseUrl}/me/day-plan").bodyOrThrow<DayPlanResponse>().plannedToday

    override suspend fun getDayPlanned(): Boolean =
        client.get("${TodoApiConfig.baseUrl}/me/day-plan").bodyOrThrow<DayPlanResponse>().plannedToday
}
