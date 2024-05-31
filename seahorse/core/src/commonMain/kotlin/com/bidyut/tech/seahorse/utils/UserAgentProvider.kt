package com.bidyut.tech.seahorse.utils

interface UserAgentProvider {
    fun get(): String
}

class SeahorseUserAgentProvider : UserAgentProvider {
    private val userAgent by lazy {
        makeUserAgent("Seahorse", "0.9.0")
    }

    override fun get() = userAgent
}

internal expect fun makeUserAgent(
    appName: String,
    appVersion: String,
): String
