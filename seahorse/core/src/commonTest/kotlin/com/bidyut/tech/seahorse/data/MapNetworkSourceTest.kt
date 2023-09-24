package com.bidyut.tech.seahorse.data

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class MapNetworkSourceTest {
    @Test
    fun `ensure we can fetch strings for all languages`() {
        val source = MapNetworkSource(
            mapOf(
                "en" to mapOf(
                    "key" to "value",
                ),
                "bn" to mapOf(
                    "key" to "মান",
                ),
            ),
        )
        runBlocking {
            source.fetchStrings("en")
        }.let {
            assertTrue(it.isSuccess)
        }
        runBlocking {
            source.fetchStrings("es")
        }.let {
            assertTrue(it.isFailure)
        }
    }
}
