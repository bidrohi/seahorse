package com.bidyut.tech.seahorse.utils

actual fun sanitiseFormatString(
    string: String,
): String = string

actual fun formatString(
    fmt: String,
    vararg args: Any,
): String = if (args.isEmpty()) {
    fmt
} else {
    fmt.format(*args)
}