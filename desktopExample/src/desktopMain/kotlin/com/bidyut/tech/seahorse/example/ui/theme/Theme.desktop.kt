package com.bidyut.tech.seahorse.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

internal actual fun isDynamicColorSupported(): Boolean {
    return false
}

@Composable
internal actual fun handleDynamicColorScheme(
    isInDarkTheme: Boolean,
): ColorScheme {
    return DarkColorScheme
}

@Composable
internal actual fun ConfigureWindow(
    isInDarkTheme: Boolean,
) {
}
