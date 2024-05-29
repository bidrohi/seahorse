package com.bidyut.tech.seahorse.utils

import com.bidyut.tech.seahorse.annotation.SeahorseInternalApi
import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

@SeahorseInternalApi
private val StringParamMatcher = Regex("(?<!%)%([1-9]\\$|)s")

@SeahorseInternalApi
actual fun sanitiseFormatString(
    string: String,
): String = StringParamMatcher.replace(string) {
    it.value.replace("s", "@")
}

@SeahorseInternalApi
actual fun formatString(
    fmt: String,
    vararg args: Any,
): String {
    return when (args.size) {
        0 -> fmt
        1 -> NSString.stringWithFormat(fmt, args[0])
        2 -> NSString.stringWithFormat(fmt, args[0], args[1])
        3 -> NSString.stringWithFormat(fmt, args[0], args[1], args[2])
        4 -> NSString.stringWithFormat(fmt, args[0], args[1], args[2], args[3])
        5 -> NSString.stringWithFormat(fmt, args[0], args[1], args[2], args[3], args[4])
        6 -> NSString.stringWithFormat(fmt, args[0], args[1], args[2], args[3], args[4], args[5])
        else -> throw IllegalArgumentException("can't handle more then 6 arguments now")
    }
}
