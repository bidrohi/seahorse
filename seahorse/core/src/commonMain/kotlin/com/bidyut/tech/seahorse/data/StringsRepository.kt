package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.annotation.SeahorseInternalApi
import com.bidyut.tech.seahorse.model.LanguageId
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

@SeahorseInternalApi
internal class StringsRepository(
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
            localStore?.replaceStrings(languageId, result.getOrThrow())
                ?.map { now }
                ?: Result.success(now)
        } else {
            Result.failure(result?.exceptionOrNull() ?: Exception("Failed to fetch strings"))
        }
    }

    suspend fun clearStore(
        languageId: LanguageId,
    ): Result<Boolean> {
        return localStore?.clearStore(languageId) ?: Result.success(true)
    }
}
