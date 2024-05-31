package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageEnglish
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class OkHttpNetworkSourceTest {
    private val mockWebServer = MockWebServer()
    private val networkSource = OkHttpNetworkSource {
        mockWebServer.url("/$it").toString()
    }

    @BeforeTest
    fun before() {
        mockWebServer.start()
    }

    @AfterTest
    fun after() {
        mockWebServer.shutdown()
    }

    @Test
    fun `ensure success fetch works`() {
        val mockedResponse = MockResponse()
            .setResponseCode(200)
            .addHeader("Content-Type", "application/json")
            .setBody(
                """{
                    "key1": "value",
                    "key2": "value %s",
                    "key3": "value %1s %2s",
                    "key4": "value %1s %2s %%"
                }""".trimIndent()
            )
        mockWebServer.enqueue(mockedResponse)

        val result = runBlocking {
            networkSource.fetchStrings(LanguageEnglish)
        }
        assertTrue(result.isSuccess)
        assertEquals(4, result.getOrNull()?.size)
    }

    @Test
    fun `ensure success fetch with invalid body`() {
        val mockedResponse = MockResponse()
            .setResponseCode(200)
            .addHeader("Content-Type", "application/json")
            .setBody(
                """{
                    "key1": "value",
                    "key2": "value %s",
                    "key3": "value %1s %2s,
                    "key4": "value %1s %2s %%"
                }""".trimIndent()
            )
        mockWebServer.enqueue(mockedResponse)

        assertFailsWith<SerializationException> {
            val result = runBlocking {
                networkSource.fetchStrings(LanguageEnglish)
            }
            assertTrue(result.isFailure)
            assertNotNull(result.exceptionOrNull())
        }
    }

    @Test
    fun `ensure failed fetch`() {
        val mockedResponse = MockResponse()
            .setResponseCode(400)
        mockWebServer.enqueue(mockedResponse)

        val result = runBlocking {
            networkSource.fetchStrings(LanguageEnglish)
        }
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }
}
