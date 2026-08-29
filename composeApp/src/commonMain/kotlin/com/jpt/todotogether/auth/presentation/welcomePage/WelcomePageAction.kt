package com.jpt.todotogether.auth.presentation.welcomePage

interface WelcomePageAction {
    object OnLoginPressed : WelcomePageAction
    object OnGetStartedPressed : WelcomePageAction
}