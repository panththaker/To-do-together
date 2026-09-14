package com.jpt.todotogether.auth.presentation.loginPage

import com.jpt.todotogether.home.presentation.homePage.HomePageAction

sealed interface LoginPageAction {
    object OnLoginPressed : LoginPageAction
    data class OnGoogleSignInSucceeded(val idToken: String) : LoginPageAction
    data class OnGoogleSignInFailed(val message: String) : LoginPageAction
    object OnApplePressed : LoginPageAction
    object OnSignInPressed : LoginPageAction
}