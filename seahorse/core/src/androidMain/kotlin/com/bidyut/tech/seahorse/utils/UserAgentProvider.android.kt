package com.bidyut.tech.seahorse.utils

import android.os.Build

internal actual fun makeUserAgent(
    appName: String,
    appVersion: String,
): String {
    val osDetails = "Android ${Build.VERSION.RELEASE}" +
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ",${Build.VERSION.SECURITY_PATCH}"
            } else ""
    return "$appName/$appVersion ($osDetails; ${Build.MODEL})"
}
