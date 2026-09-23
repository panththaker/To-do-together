package com.jpt.todotogether.core.domain.repository

interface DayPlanRepository {
    // records that the user planned today; returns the new plannedToday state.
    suspend fun markDayPlanned(): Boolean
    suspend fun getDayPlanned(): Boolean
}
