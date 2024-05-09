package com.bidyut.tech.seahorse.utils


expect fun sanitiseFormatString(string: String): String

expect fun formatString(
    fmt: String,
    vararg args: Any,
): String
