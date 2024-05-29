package com.bidyut.tech.seahorse.utils

import com.bidyut.tech.seahorse.annotation.SeahorseInternalApi

@SeahorseInternalApi
actual fun sanitiseFormatString(
    string: String,
): String = string

@SeahorseInternalApi
actual fun formatString(
    fmt: String,
    vararg args: Any,
): String = if (args.isEmpty()) {
    fmt
} else {
    fmt.format(*args)
}
