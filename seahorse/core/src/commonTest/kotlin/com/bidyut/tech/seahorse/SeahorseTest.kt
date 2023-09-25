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
    fun `successfully initialise Seahorse`() {
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
                        "key" to "networkValue",
                        "keyWithParam" to "%s networkValue",
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
        assertEquals("networkValue", seahorse.getStringForLanguage("bn", "key"))
        assertEquals("param networkValue", seahorse.getStringForLanguage("bn", "keyWithParam", "param"))
        runBlocking {
            seahorse.fetchStrings("bn")
            seahorse.fetchStrings("es")
        }
    }

    @Test()
    fun `Seahorse needs fallback source`() {
        assertFailsWith(IllegalArgumentException::class) {
            Seahorse {
                defaultLanguageId = "en"
            }
        }
    }
}
