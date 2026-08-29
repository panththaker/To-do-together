package com.jpt.todotogether.di

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.jpt.todotogether.AppDatabase
import com.jpt.todotogether.Settings
import com.jpt.todotogether.auth.presentation.welcomePage.WelcomePageViewModel
import com.jpt.todotogether.core.data.auth.TokenStorage
import com.jpt.todotogether.core.data.remote.TodoApiConfig
import com.jpt.todotogether.core.data.remote.dto.TokenRequest
import com.jpt.todotogether.core.data.remote.dto.TokenResponse
import com.jpt.todotogether.core.data.repository.KtorAuthRepository
import com.jpt.todotogether.core.data.repository.KtorTodoRepository
import com.jpt.todotogether.core.data.repository.SQLDelightSettingsRepository
import com.jpt.todotogether.core.domain.repository.AuthRepository
import com.jpt.todotogether.core.domain.repository.SettingsRepository
import com.jpt.todotogether.core.domain.repository.TodoRepository
import com.jpt.todotogether.home.presentation.homePage.HomePageViewModel
import com.jpt.todotogether.logger.AppLogger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

// expect declaration for the platform-specific module, this is where you can define any platform-specific dependencies
// ie the SQLDelight driver
expect val platformModule: Module

// the shared DI module on all platforms
val sharedModule = module {

    // this single block is used to define the SQLDelight database and
    // relevant column adapters etc
    single {

        // fetch the platform specific driver
        val driver = get<SqlDriver>()

        // init db
        val db = AppDatabase(
            driver,
            // adapter to allow kotlin enum as data type in our DB
            SettingsAdapter = Settings.Adapter(
                themeAdapter = EnumColumnAdapter()
            ),
        )

        // run the settings initialization
        db.appDatabaseQueries.initializeSettingsOrIgnore()

        return@single db
    }

    // persists the signed-in user's access/refresh token pair; shared between
    // AuthRepository and the HttpClient's bearer Auth provider below so both
    // read and write the same row (see TokenStorage's doc comment).
    single { TokenStorage(db = get()) }

    // the shared, fully configured Ktor client. Each platform's `platformModule`
    // (see Modules.android.kt / Modules.ios.kt / Modules.jvm.kt) supplies the
    // `HttpClientEngine` (OkHttp on Android/JVM, Darwin on iOS) — the same
    // expect/actual split already used above for the SqlDriver.
    single<HttpClient> {
        val engine = get<HttpClientEngine>()
        val tokenStorage = get<TokenStorage>()

        HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        AppLogger.debug(tag = "Ktor", message = message)
                    }
                }
                level = LogLevel.INFO
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 15_000
            }

            // attaches the stored access token to outgoing requests, and
            // transparently rotates it via POST /auth/token on a 401 - the
            // same refresh flow AuthRepository uses for its own proactive
            // expiry check, but this catches revocation/clock-skew cases too.
            install(Auth) {
                bearer {
                    loadTokens {
                        val session = tokenStorage.getSession()
                        if (session == null) {
                            return@loadTokens null
                        }
                        return@loadTokens BearerTokens(session.accessToken, session.refreshToken)
                    }

                    refreshTokens {
                        val current = tokenStorage.getSession() ?: return@refreshTokens null
                        val response = runCatching {
                            this.client.post("${TodoApiConfig.baseUrl}/auth/token") {
                                contentType(ContentType.Application.Json)
                                setBody(TokenRequest(grant_type = "refresh_token", refresh_token = current.refreshToken))
                            }.body<TokenResponse>()
                        }.getOrElse {
                            tokenStorage.clear()
                            return@refreshTokens null
                        }

                        val session = tokenStorage.save(response)
                        BearerTokens(session.accessToken, session.refreshToken)
                    }

                    sendWithoutRequest { request ->
                        request.url.pathSegments.lastOrNull() != "token"
                    }
                }
            }
        }
    }

    // bind repositories
    singleOf(::SQLDelightSettingsRepository) bind SettingsRepository::class
    singleOf(::KtorTodoRepository) bind TodoRepository::class
    singleOf(::KtorAuthRepository) bind AuthRepository::class

    // define view models
    viewModelOf(::HomePageViewModel)
    viewModelOf(::WelcomePageViewModel)
    // ... add other view models here
    // viewModelOf(::SetupPageViewModel)
    // viewModelOf(::SettingsPageViewModel)

    // more complex view model definitions can be made, for example
    // this is how you would define a view model that takes parameters (often useful for routing)
    // viewModel {
    //     (envelopeId: Long) ->
    //         InfoPageViewModel(
    //             envelopeId,
    //             get(),
    //             get()
    //         )
    // }
}
