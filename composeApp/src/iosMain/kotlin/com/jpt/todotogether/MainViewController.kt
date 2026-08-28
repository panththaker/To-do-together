package com.jpt.todotogether

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

import com.jpt.todotogether.core.App
import com.jpt.todotogether.di.initKoin

private var isAppInitialized = false

fun MainViewController(): UIViewController {
    if (!isAppInitialized) {
        initKoin()
        isAppInitialized = true
    }
    return ComposeUIViewController { App() }
}