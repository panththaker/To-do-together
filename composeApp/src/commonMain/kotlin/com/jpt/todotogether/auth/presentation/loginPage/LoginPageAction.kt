package com.jpt.todotogether.auth.presentation.loginPage

interface LoginPageAction {
    object OnLoginPressed : LoginPageAction
    object OnGooglePressed : LoginPageAction
    object OnApplePressed : LoginPageAction
    object OnSignInPressed : LoginPageAction
}