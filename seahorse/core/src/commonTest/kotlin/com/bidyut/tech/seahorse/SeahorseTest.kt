package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.data.MapFallbackSource
import com.bidyut.tech.seahorse.data.MapLocalSourceSink
import com.bidyut.tech.seahorse.data.MapNetworkSource
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SeahorseTest {
    @Test
    fun `successfully initialise Seahorse`() {
        val seahorse = Seahorse {
            defaultLanguageId = "en"
            fallbackSource = MapFallbackSource(
                mapOf(
                    "en" to mapOf(
                        "key" to "value",
                        "fallbackKey" to "fallbackValue"
                    ),
                )
            )
            val sourceSink = MapLocalSourceSink()
            localSource = sourceSink
            localSink = sourceSink
            networkSource = MapNetworkSource(
                mapOf(
                    "bn" to mapOf(
                        "key" to "networkValue",
                        "keyWithParam" to "%s value"
                    ),
                )
            )
        }
        assertEquals("en", seahorse.defaultLanguageId)
        assertEquals("value", seahorse.getString("key", "param"))
        runBlocking {
            seahorse.fetchStrings("bn")
        }
        seahorse.defaultLanguageId = "es"
        assertEquals("networkValue", seahorse.getStringForLanguage("bn", "key"))
        assertEquals("param value", seahorse.getStringForLanguage("bn", "keyWithParam", "param"))
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

    @Test()
    fun `Seahorse needs local source`() {
        assertFailsWith(IllegalArgumentException::class) {
            Seahorse {
                defaultLanguageId = "en"
                fallbackSource = MapFallbackSource(
                    mapOf(
                        "en" to mapOf(
                            "key" to "value",
                            "fallbackKey" to "fallbackValue"
                        ),
                    )
                )
            }
        }
    }
}
