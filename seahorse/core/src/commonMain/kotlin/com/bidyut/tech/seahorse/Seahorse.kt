package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.data.StringsRepository
import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlin.experimental.ExperimentalObjCName
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.ObjCName
import kotlin.native.ShouldRefineInSwift
import kotlin.time.Duration

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
    ): Result<Instant> {
        return repository.fetchStrings(
            languageId,
            cacheInterval,
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
    ): Boolean {
        return runBlocking(Dispatchers.IO) {
            for (language in languages) {
                val result = fetchStrings(language)
                if (result.isFailure) {
                    return@runBlocking false
                }
            }
            true
        }
    }

    fun clearStore(
        @ObjCName("_")
        languages: List<LanguageId>,
    ): Boolean {
        return runBlocking(Dispatchers.IO) {
            for (language in languages) {
                val result = clearStore(language)
                if (result.isFailure) {
                    return@runBlocking false
                }
            }
            true
        }
    }
}
