package com.bidyut.tech.seahorse.utils

internal actual fun makeUserAgent(
    appName: String,
    appVersion: String,
): String {
    val vmDetails = "JVM ${System.getProperty("java.version")}"
    return "$appName/$appVersion ($vmDetails)"
}
