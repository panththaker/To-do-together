package com.jpt.todotogether.di

import com.jpt.todotogether.core.data.auth.GoogleAuthConfig
import com.mmk.kmpauth.core.KMPAuth
import com.mmk.kmpauth.google.google
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, platformModule)
    }

    KMPAuth.initialize {
        google(serverId = GoogleAuthConfig.WEB_CLIENT_ID, redirectUri = GoogleAuthConfig.DESKTOP_REDIRECT_URI)
    }
}