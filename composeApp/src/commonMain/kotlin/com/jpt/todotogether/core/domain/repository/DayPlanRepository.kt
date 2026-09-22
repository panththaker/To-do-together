package com.jpt.todotogether.core.domain.repository

interface DayPlanRepository {
    suspend fun markDayPlanned(): Boolean
    suspend fun getDayPlanned(): Boolean?
}