package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.data.StringsRepository
import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlin.experimental.ExperimentalObjCName
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.ObjCName
import kotlin.native.ShouldRefineInSwift
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@OptIn(ExperimentalObjCName::class, ExperimentalObjCRefinement::class)
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

    @ShouldRefineInSwift
    fun getStringForLanguage(
        @ObjCName("_")
        languageId: LanguageId,
        @ObjCName("_")
        key: String,
        vararg formatArgs: Any,
    ): String = repository.getStringByKey(languageId, key, *formatArgs)

    fun getStringForLanguage(
        @ObjCName("_")
        languageId: LanguageId,
        @ObjCName("_")
        key: String,
    ): String = repository.getStringByKey(languageId, key)

    @ShouldRefineInSwift
    fun getString(
        @ObjCName("_")
        key: String,
        vararg formatArgs: Any,
    ): String = getStringForLanguage(defaultLanguageId, key, *formatArgs)

    fun getString(
        @ObjCName("_")
        key: String,
    ): String = getStringForLanguage(defaultLanguageId, key)

    @ShouldRefineInSwift
    suspend fun fetchStrings(
        @ObjCName("_")
        languageId: LanguageId,
        forceUpdate: Boolean = false,
    ): Result<Instant> {
        return repository.fetchStrings(
            languageId,
            if (forceUpdate) 0.seconds else cacheInterval,
        )
    }

    @ShouldRefineInSwift
    suspend fun clearStore(
        @ObjCName("_")
        languageId: LanguageId,
    ) = repository.clearStore(languageId)

    fun refreshStrings(
        @ObjCName("_")
        languages: List<LanguageId>,
    ): Int {
        return runBlocking(Dispatchers.IO) {
            var success = 0
            for (language in languages) {
                val result = fetchStrings(language, forceUpdate = true)
                if (!result.isFailure) {
                    success++
                }
            }
            success
        }
    }

    fun clearStore(
        @ObjCName("_")
        languages: List<LanguageId>,
    ): Int {
        return runBlocking(Dispatchers.IO) {
            var success = 0
            for (language in languages) {
                val result = clearStore(language)
                if (!result.isFailure) {
                    success++
                }
            }
            success
        }
    }
}
