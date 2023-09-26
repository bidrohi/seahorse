package com.bidyut.tech.seahorse.data

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes

class StringsRepositoryTest {
    @Test
    fun `get strings from fallback`() {
        val fallback = MapFallbackSource(
            mapOf(
                "en" to mapOf(
                    "key" to "value",
                ),
            )
        )
        val repository = StringsRepository(fallback, null, null)
        assertEquals("value", repository.getStringByKey("en", "key"))
        assertEquals("missingKey", repository.getStringByKey("en", "missingKey"))
        runBlocking {
            repository.fetchStrings("en", 1.minutes)
        }
    }

    @Test
    fun `get strings from source`() {
        val fallback = MapFallbackSource(
            mapOf(
                "en" to mapOf(
                    "key" to "value",
                    "fallbackKey" to "fallbackValue",
                ),
            )
        )
        val localStore = MapLocalStore()
        runBlocking {
            localStore.replaceStrings("en", mapOf("key" to "store value"))
        }
        val repository = StringsRepository(fallback, localStore, null)
        assertEquals("store value", repository.getStringByKey("en", "key"))
        assertEquals("fallbackValue", repository.getStringByKey("en", "fallbackKey"))
        assertEquals("missingKey", repository.getStringByKey("en", "missingKey"))
        runBlocking {
            repository.fetchStrings("en", 1.minutes)
        }
    }

    @Test
    fun `fetch strings from network`() {
        val fallback = MapFallbackSource(
            mapOf(
                "en" to mapOf(
                    "key" to "value",
                    "fallbackKey" to "fallbackValue",
                ),
            )
        )
        val localStore = MapLocalStore()
        val networkSource = MapNetworkSource(
            mapOf(
                "en" to mapOf(
                    "key" to "networkValue",
                ),
                "bn" to mapOf(
                    "missingKey" to "উধাত্ত",
                ),
            )
        )
        val repository = StringsRepository(fallback, localStore, networkSource)
        assertEquals("value", repository.getStringByKey("en", "key"))
        runBlocking {
            repository.fetchStrings("en", 1.minutes)
        }
        assertEquals("networkValue", repository.getStringByKey("en", "key"))
        assertEquals("fallbackValue", repository.getStringByKey("en", "fallbackKey"))
        assertEquals("missingKey", repository.getStringByKey("bn", "missingKey"))
        runBlocking {
            repository.fetchStrings("bn", 1.minutes)
        }
        assertEquals("উধাত্ত", repository.getStringByKey("bn", "missingKey"))
    }
}
