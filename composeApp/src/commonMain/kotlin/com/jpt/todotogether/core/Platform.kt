package com.jpt.todotogether.core

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform