package com.jpt.todotogether.auth.presentation.loginPage

import com.jpt.todotogether.core.domain.model.AuthSession
import com.jpt.todotogether.core.domain.model.AuthUser

data class LoginPageState(
    val isLoading: Boolean = false,
    val authUser: AuthUser? = null,
    val isSigningIn: Boolean = false,
    val authError: String? = null,
    val authSession: AuthSession? = null,
)
