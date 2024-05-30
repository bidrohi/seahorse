@file:OptIn(ExperimentalObjCName::class)

package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDate
import platform.Foundation.NSTimeInterval
import kotlin.coroutines.cancellation.CancellationException
import kotlin.experimental.ExperimentalObjCName

fun Seahorse.getStringForLanguage(
    @ObjCName("_")
    languageId: LanguageId,
    @ObjCName("_")
    key: String,
    args: List<String>,
): String = getStringForLanguage(languageId, key, *args.toTypedArray())

fun Seahorse.getString(
    @ObjCName("_")
    key: String,
    args: List<String>,
): String = getStringForLanguage(defaultLanguageId, key, *args.toTypedArray())

class FetchStringsFailureException(
    cause: Throwable? = null,
) : Exception("Failed to fetch strings", cause)

@Throws(CancellationException::class, FetchStringsFailureException::class)
suspend fun Seahorse.fetchStringsAsync(
    @ObjCName("_")
    languageId: LanguageId,
): NSDate {
    val result = fetchStrings(languageId)
    return if (result.isSuccess) {
        result.getOrThrow().toNSDate()
    } else {
        throw FetchStringsFailureException(result.exceptionOrNull())
    }
}

fun Seahorse.getCacheInterval(): NSTimeInterval {
    return cacheInterval.inWholeSeconds.toDouble()
}
