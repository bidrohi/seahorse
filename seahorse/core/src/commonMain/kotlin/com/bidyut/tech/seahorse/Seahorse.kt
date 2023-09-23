package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.data.StringsRepository
import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class Seahorse(
    configBuilder: SeahorseConfig.Builder.() -> Unit,
) {
    private val config = SeahorseConfig.Builder()
        .apply(configBuilder)
        .build()

    private val repository = StringsRepository(
        config.localSource,
        config.fallbackSource,
        config.defaultLanguageId,
    )

    var defaultLanguageId: LanguageId
        get() = config.defaultLanguageId
        set(value) {
            config.fallbackSource.setLanguageId(value)
            config.defaultLanguageId = value
        }

    fun getString(
        key: String,
        vararg formatArgs: Any,
        languageId: LanguageId = config.defaultLanguageId,
    ): String = repository.getStringByKey(languageId, key, *formatArgs)

    fun getString(
        key: String,
        vararg formatArgs: Any,
    ): String = getString(key, *formatArgs, languageId = config.defaultLanguageId)

    fun getString(
        key: String,
    ): String = getString(key, languageId = config.defaultLanguageId)

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
