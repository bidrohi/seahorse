package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.data.StringsRepository
import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class Seahorse(
    configBuilder: SeahorseConfig.Builder.() -> Unit,
) {
    internal val config = SeahorseConfig.Builder()
        .apply(configBuilder)
        .build()

    private val repository = StringsRepository(
        config.localSource,
        config.fallbackSource,
    )

    var defaultLanguageId: LanguageId
        get() = config.defaultLanguageId
        set(value) {
            config.fallbackSource.setLanguageId(value)
            config.defaultLanguageId = value
        }

    fun getStringForLanguage(
        languageId: LanguageId,
        key: String,
        vararg formatArgs: Any,
    ): String = repository.getStringByKey(languageId, key, *formatArgs)

    fun getStringForLanguage(
        languageId: LanguageId,
        key: String,
    ): String = repository.getStringByKey(languageId, key)

    fun getString(
        key: String,
        vararg formatArgs: Any,
    ): String = getStringForLanguage(config.defaultLanguageId, key, *formatArgs)

    fun getString(
        key: String,
    ): String = getStringForLanguage(config.defaultLanguageId, key)

    suspend fun fetchStrings(
        languageId: LanguageId,
    ): Result<Instant> {
        val now = Clock.System.now()
        config.localSink?.lastUpdateTime?.get(languageId)?.let {
            if (it.plus(config.cacheInterval) > now) {
                return Result.success(it)
            }
        }
        val result = config.networkSource?.fetchStrings(languageId)
        return if (result?.isSuccess == true) {
            config.localSink?.storeStrings(languageId, result.getOrThrow())
            Result.success(now)
        } else {
            Result.failure(result?.exceptionOrNull() ?: Exception("Failed to fetch strings"))
        }
    }
}
