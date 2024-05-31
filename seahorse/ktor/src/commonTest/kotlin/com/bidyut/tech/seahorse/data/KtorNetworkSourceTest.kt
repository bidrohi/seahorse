package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageEnglish
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.JsonConvertException
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KtorNetworkSourceTest {
    @Test
    fun `ensure success fetch works`() {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(
                    """{
                        "key1": "value",
                        "key2": "value %s",
                        "key3": "value %1s %2s",
                        "key4": "value %1s %2s %%"
                    }""".trimIndent()
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val networkSource = KtorNetworkSource(mockEngine) { it }

        val result = runBlocking {
            networkSource.fetchStrings(LanguageEnglish)
        }
        assertTrue(result.isSuccess)
        assertEquals(4, result.getOrNull()?.size)
    }

    @Test
    fun `ensure success fetch with invalid body`() {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(
                    """{
                        "key1": "value",
                        "key2": "value %s",
                        "key3": "value %1s %2s,
                        "key4": "value %1s %2s %%"
                    }""".trimIndent()
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val networkSource = KtorNetworkSource(mockEngine) { it }

        assertFailsWith<JsonConvertException> {
            val result = runBlocking {
                networkSource.fetchStrings(LanguageEnglish)
            }
            assertTrue(result.isFailure)
            assertNotNull(result.exceptionOrNull())
        }
    }

    @Test
    fun `ensure failed fetch`() {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.BadRequest,
            )
        }
        val networkSource = KtorNetworkSource(mockEngine) { it }

        val result = runBlocking {
            networkSource.fetchStrings(LanguageEnglish)
        }
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }
}
