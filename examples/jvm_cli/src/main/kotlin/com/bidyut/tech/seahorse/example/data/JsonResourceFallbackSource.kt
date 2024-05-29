package com.bidyut.tech.seahorse.example.data

import com.bidyut.tech.seahorse.data.JsonFallbackSource
import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class JsonResourceFallbackSource : JsonFallbackSource() {
    override fun readStrings(languageId: LanguageId): Map<String, String> {
        return runBlocking(Dispatchers.IO) {
            javaClass.getResource("/files/$languageId.json")?.readBytes()?.let { bytes ->
                parseStrings(bytes.decodeToString())
            } ?: emptyMap()
        }
    }
}
