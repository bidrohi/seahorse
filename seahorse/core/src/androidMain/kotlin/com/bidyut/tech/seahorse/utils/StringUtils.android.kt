package com.bidyut.tech.seahorse.utils

actual fun formatString(
    fmt: String,
    vararg args: Any,
): String = fmt.format(*args)
