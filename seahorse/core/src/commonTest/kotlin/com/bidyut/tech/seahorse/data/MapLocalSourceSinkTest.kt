package com.bidyut.tech.seahorse.data

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MapLocalSourceSinkTest {
    @Test
    fun `ensure we can add strings works for all languages`() {
        val sourceSink = MapLocalSourceSink()
        assertNull(sourceSink.getStringByKey("en", "key"))
        runBlocking {
            sourceSink.storeStrings(
                "en",
                mapOf(
                    "key" to "value",
                ),
            )
        }
        assertEquals("value", sourceSink.getStringByKey("en", "key"))
        assertNull(sourceSink.getStringByKey("en", "missingKey"))
        assertNull(sourceSink.getStringByKey("bn", "key"))
    }
}
