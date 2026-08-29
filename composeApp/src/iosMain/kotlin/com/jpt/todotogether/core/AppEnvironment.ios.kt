package com.jpt.todotogether.core

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform as NativePlatform

@OptIn(ExperimentalNativeApi::class)
actual val isDebugBuild: Boolean = NativePlatform.isDebugBinary
