package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.data.StringsRepository
import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.datetime.Instant
import kotlin.time.Duration

class Seahorse(
    configBuilder: SeahorseConfig.Builder.() -> Unit,
) {
    private val repository: StringsRepository
    internal val cacheInterval: Duration

    var defaultLanguageId: LanguageId = LanguageEnglish
        set(value) {
            repository.fallback.setLanguageId(value)
            field = value
        }

    init {
        val config = SeahorseConfig.Builder()
            .apply(configBuilder)
            .build()
        repository = StringsRepository(
            config.fallbackSource,
            config.localStore,
            config.networkSource,
        )
        cacheInterval = config.cacheInterval
        defaultLanguageId = config.defaultLanguageId
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
    ): String = getStringForLanguage(defaultLanguageId, key, *formatArgs)

    fun getString(
        key: String,
    ): String = getStringForLanguage(defaultLanguageId, key)

    suspend fun fetchStrings(
        languageId: LanguageId,
    ): Result<Instant> {
        return repository.fetchStrings(
            languageId,
            cacheInterval,
        )
    }
}
