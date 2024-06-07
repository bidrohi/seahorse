package com.bidyut.tech.seahorse.utils

import com.bidyut.tech.seahorse.annotation.SeahorseInternalApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.cstr
import kotlinx.cinterop.toKStringFromUtf8
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
    val strArgs = args.map {
        it.toString()
    }
    val maxLen = fmt.cstr.size + strArgs.sumOf { it.cstr.size }
    return ByteArray(maxLen).usePinned { pinned ->
        val outBuf = pinned.addressOf(0)
        val bufLen = maxLen.convert<size_t>()
        val size = when (args.size) {
            1 -> snprintf(outBuf, bufLen, fmt, strArgs[0])
            2 -> snprintf(outBuf, bufLen, fmt, strArgs[0], strArgs[1])
            3 -> snprintf(outBuf, bufLen, fmt, strArgs[0], strArgs[1], strArgs[2])
            4 -> snprintf(outBuf, bufLen, fmt, strArgs[0], strArgs[1], strArgs[2], strArgs[3])
            5 -> snprintf(outBuf, bufLen, fmt, strArgs[0], strArgs[1], strArgs[2], strArgs[3], strArgs[4])
            6 -> snprintf(outBuf, bufLen, fmt, strArgs[0], strArgs[1], strArgs[2], strArgs[3], strArgs[4], strArgs[5])
            else -> throw IllegalArgumentException("can't handle more then 6 arguments now")
        }
        if (size < 0 || size > maxLen) {
            throw IllegalArgumentException("Failed to format string, needed $size, have $maxLen: $fmt")
        }
        outBuf.toKStringFromUtf8()
    }
}
