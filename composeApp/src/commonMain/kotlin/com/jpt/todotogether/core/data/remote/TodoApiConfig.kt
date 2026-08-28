package com.jpt.todotogether.core.data.remote

import com.jpt.todotogether.BuildKonfig
import com.jpt.todotogether.core.isDebugBuild

// Centralizes the Todo API's base URL so debug builds talk to each
// developer's own local backend and release builds talk to production.
// DEBUG_BASE_URL comes from the gitignored local.properties (key
// "debug.baseUrl") via BuildKonfig — see README.md "Configuration".
object TodoApiConfig {
    private val DEBUG_BASE_URL = BuildKonfig.DEBUG_BASE_URL
    private const val RELEASE_BASE_URL = "https://api.todotogether.example.com"

    val baseUrl: String = if (isDebugBuild) DEBUG_BASE_URL else RELEASE_BASE_URL
}
