package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

class StringsRepository(
    internal val fallback: FallbackSource,
    private val localStore: LocalStore?,
    private val networkSource: NetworkSource?,
) {
    fun getStringByKey(
        languageId: LanguageId,
        key: String,
        vararg formatArgs: Any,
    ): String = localStore?.getStringByKey(languageId, key, *formatArgs)
        ?: fallback.getStringByKey(key, *formatArgs)
        ?: key

    suspend fun fetchStrings(
        languageId: LanguageId,
        cacheInterval: Duration,
    ): Result<Instant> {
        val now = Clock.System.now()
        localStore?.getLastUpdatedTime(languageId)?.let {
            if (it.plus(cacheInterval) > now) {
                return Result.success(it)
            }
        }
        val result = networkSource?.fetchStrings(languageId)
        return if (result?.isSuccess == true) {
            localStore?.storeStrings(languageId, result.getOrThrow())
            Result.success(now)
        } else {
            Result.failure(result?.exceptionOrNull() ?: Exception("Failed to fetch strings"))
        }
    }
}
