package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.data.MapFallbackSource
import com.bidyut.tech.seahorse.data.MapLocalStore
import com.bidyut.tech.seahorse.data.MapNetworkSource
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.days

class SeahorseTest {
    @Test
    fun `successfully initialise and use Seahorse`() {
        val seahorse = Seahorse {
            defaultLanguageId = "en"
            fallbackSource = MapFallbackSource(
                mapOf(
                    "en" to mapOf(
                        "key" to "value",
                        "keyWithParam" to "%s value",
                        "fallbackKey" to "fallbackValue",
                    ),
                )
            )
            localStore = MapLocalStore()
            networkSource = MapNetworkSource(
                mapOf(
                    "bn" to mapOf(
                        "key" to "নেটওয়ার্ক",
                        "keyWithParam" to "%s নেটওয়ার্ক",
                    ),
                    "cn" to mapOf(
                        "key" to "网络",
                    ),
                    "in" to mapOf(
                        "key" to "jaringan",
                    ),
                )
            )
        }
        assertEquals("en", seahorse.defaultLanguageId)
        assertEquals(1.days, seahorse.cacheInterval)
        assertEquals("value", seahorse.getString("key"))
        assertEquals("param value", seahorse.getString("keyWithParam", "param"))
        runBlocking {
            seahorse.fetchStrings("bn")
        }
        seahorse.defaultLanguageId = "es"
        assertEquals("es", seahorse.defaultLanguageId)
        assertEquals("নেটওয়ার্ক", seahorse.getStringForLanguage("bn", "key"))
        assertEquals(
            "param নেটওয়ার্ক",
            seahorse.getStringForLanguage("bn", "keyWithParam", "param")
        )
        val refreshCount = runBlocking {
            seahorse.refreshStrings(listOf("cn", "es", "in"))
        }
        assertEquals(2, refreshCount)
        assertEquals("key", seahorse.getString("key"), "Should fallback to key")
        runBlocking {
            seahorse.clearStore("bn")
        }
        assertEquals("key", seahorse.getStringForLanguage("bn", "key"))
        assertEquals("网络", seahorse.getStringForLanguage("cn", "key"))
        assertEquals("jaringan", seahorse.getStringForLanguage("in", "key"))
        val clearCount = runBlocking {
            seahorse.clearStore(listOf("cn", "id"))
        }
        assertEquals(2, clearCount)
        assertEquals("key", seahorse.getStringForLanguage("cn", "key"))
        assertEquals("key", seahorse.getStringForLanguage("id", "key"))
    }

    @Test()
    fun `Seahorse needs fallback source`() {
        assertFailsWith(IllegalArgumentException::class) {
            Seahorse {
                defaultLanguageId = "en"
            }
        }
    }

    @Test()
    fun `Seahorse needs local store for network source`() {
        assertFailsWith(IllegalArgumentException::class) {
            Seahorse {
                fallbackSource = MapFallbackSource(emptyMap())
                networkSource = MapNetworkSource(emptyMap())
            }
        }
    }

    @Test()
    fun `Seahorse can function with just fallback source`() {
        val seahorse = Seahorse {
            defaultLanguageId = "en"
            fallbackSource = MapFallbackSource(
                mapOf(
                    "en" to mapOf(
                        "key" to "value",
                        "keyWithParam" to "%s value",
                        "fallbackKey" to "fallbackValue",
                    ),
                )
            )
        }
        assertEquals("en", seahorse.defaultLanguageId)
        assertEquals(1.days, seahorse.cacheInterval)
        assertEquals("value", seahorse.getString("key"))
        assertEquals("param value", seahorse.getString("keyWithParam", "param"))

        runBlocking {
            seahorse.fetchStrings("en")
        }
    }
}
