package com.bidyut.tech.seahorse.utils

import platform.UIKit.UIDevice

internal actual fun makeUserAgent(
    appName: String,
    appVersion: String,
): String {
    val currentDevice = UIDevice.currentDevice
    val osDetails = "iOS ${currentDevice.systemVersion}"
    return "$appName/$appVersion ($osDetails; ${currentDevice.model})"
}
