package com.jpt.todotogether.core.data.remote

import com.jpt.todotogether.core.isDebugBuild

// Centralizes the Todo API's base URL so debug builds talk to staging and
// release builds talk to production. Point these at your real backend.
object TodoApiConfig {
    private const val DEBUG_BASE_URL = "http://192.168.0.182:8080"
    private const val RELEASE_BASE_URL = "https://api.todotogether.example.com"

    val baseUrl: String = if (isDebugBuild) DEBUG_BASE_URL else RELEASE_BASE_URL
}
