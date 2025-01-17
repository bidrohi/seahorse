package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class ByteArrayFallbackSource(
    private val toByteArray: suspend (LanguageId) -> ByteArray,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
) : JsonFallbackSource() {
    override fun readStrings(
        languageId: LanguageId,
    ): Map<String, String> {
        return runBlocking(coroutineContext) {
            val bytes = toByteArray(languageId)
            parseStrings(bytes.decodeToString())
        }
    }
}
