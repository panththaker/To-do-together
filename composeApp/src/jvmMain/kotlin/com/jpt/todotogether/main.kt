package com.jpt.todotogether

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.jpt.todotogether.core.App
import com.jpt.todotogether.di.initKoin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Sample",
    ) {
        initKoin()

        App()
    }
}