package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDate
import kotlin.coroutines.cancellation.CancellationException

class FetchStringsFailureException(
    cause: Throwable? = null,
) : Exception("Failed to fetch strings", cause)

@Throws(CancellationException::class, FetchStringsFailureException::class)
suspend fun Seahorse.fetchStringsAsync(
    languageId: LanguageId,
): NSDate {
    val result = fetchStrings(languageId)
    return if (result.isSuccess) {
        result.getOrThrow().toNSDate()
    } else {
        throw FetchStringsFailureException(result.exceptionOrNull())
    }
}
