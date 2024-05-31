package com.bidyut.tech.seahorse.utils

import platform.Foundation.NSProcessInfo

internal actual fun makeUserAgent(
    appName: String,
    appVersion: String,
): String {
    val processInfo = NSProcessInfo.processInfo()
    val osDetails = "${processInfo.operatingSystemName()} ${processInfo.operatingSystemVersionString()}"
    return "$appName/$appVersion ($osDetails; ${processInfo.hostName})"
}
