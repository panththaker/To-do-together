package com.jpt.todotogether.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.jpt.todotogether.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<SqlDriver> {
            NativeSqliteDriver(AppDatabase.Schema, "sample.db")
        }
    }