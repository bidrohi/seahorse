package com.bidyut.tech.seahorse.utils

import com.bidyut.tech.seahorse.annotation.SeahorseInternalApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.posix.size_t
import platform.posix.snprintf

@SeahorseInternalApi
private val StringParamMatcher = Regex("(?<!%)%([1-9]\\$|)s")

@SeahorseInternalApi
actual fun sanitiseFormatString(
    string: String,
): String = StringParamMatcher.replace(string) {
    "%s"
}

@OptIn(ExperimentalForeignApi::class)
@SeahorseInternalApi
actual fun formatString(
    fmt: String,
    vararg args: Any,
): String {
    if (args.isEmpty()) {
        return fmt
    }
    val maxLen = fmt.length + args.sumOf { it.toString().length }
    val buf = ByteArray(maxLen)
    buf.usePinned { pinned ->
        val outBuf = pinned.addressOf(0)
        val bufLen = maxLen.convert<size_t>()
        val size = when (args.size) {
            1 -> snprintf(outBuf, bufLen, fmt, args[0].toString())
            2 -> snprintf(outBuf, bufLen, fmt, args[0].toString(), args[1].toString())
            3 -> snprintf(outBuf, bufLen, fmt, args[0].toString(), args[1].toString(), args[2].toString())
            4 -> snprintf(outBuf, bufLen, fmt, args[0].toString(), args[1].toString(), args[2].toString(), args[3].toString())
            5 -> snprintf(outBuf, bufLen, fmt, args[0].toString(), args[1].toString(), args[2].toString(), args[3].toString(), args[4].toString())
            6 -> snprintf(outBuf, bufLen, fmt, args[0].toString(), args[1].toString(), args[2].toString(), args[3].toString(), args[4].toString(), args[5].toString())
            else -> throw IllegalArgumentException("can't handle more then 6 arguments now")
        }
        if (size < 0) {
            throw IllegalArgumentException("Failed to format string: $fmt")
        }
    }
    return buf.decodeToString()
}
