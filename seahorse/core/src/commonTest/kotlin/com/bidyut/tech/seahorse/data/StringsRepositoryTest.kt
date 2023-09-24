package com.bidyut.tech.seahorse.data

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class StringsRepositoryTest {
    @Test
    fun `get strings from source`() {
        val fallback = MapFallbackSource(
            mapOf(
                "en" to mapOf(
                    "key" to "value",
                    "fallbackKey" to "fallbackValue"
                ),
            )
        )
        val source = MapLocalSourceSink()
        runBlocking {
            source.storeStrings("en", mapOf("key" to "sink value"))
        }
        val repository = StringsRepository(source, fallback)
        assertEquals("sink value", repository.getStringByKey("en", "key"))
        assertEquals("fallbackValue", repository.getStringByKey("en", "fallbackKey"))
        assertEquals("missingKey", repository.getStringByKey("en", "missingKey"))
    }
}
