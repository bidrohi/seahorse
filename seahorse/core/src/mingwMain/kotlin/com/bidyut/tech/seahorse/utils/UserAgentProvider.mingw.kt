package com.bidyut.tech.seahorse.utils

internal actual fun makeUserAgent(
    appName: String,
    appVersion: String,
): String {
    val osDetails = "Mingw"
    return "$appName/$appVersion ($osDetails)"
}
