package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

open class KtorNetworkSource(
    private val client: HttpClient,
    private val getUrlForLanguageId: (LanguageId) -> String,
) : NetworkSource {
    constructor(
        getUrlForLanguageId: (LanguageId) -> String,
        engine: HttpClientEngine,
        config: HttpClientConfig<*>.() -> Unit,
    ) : this(
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(ContentEncoding) {
                deflate(1.0F)
                gzip(0.9F)
            }
            install(UserAgent) {
                agent = "Seahorse/0.9.0"
            }
            install(HttpCache)
            config()
        },
        getUrlForLanguageId,
    )

    constructor(
        getUrlForLanguageId: (LanguageId) -> String,
        engineFactory: HttpClientEngineFactory<*>,
        clientConfig: HttpClientConfig<*>.() -> Unit,
    ) : this(
        getUrlForLanguageId,
        engineFactory.create(),
        clientConfig
    )

    constructor(
        engine: HttpClientEngine,
        getUrlForLanguageId: (LanguageId) -> String,
    ) : this(
        getUrlForLanguageId,
        engine,
        {},
    )

    override suspend fun fetchStrings(
        languageId: LanguageId,
    ): Result<Map<String, String>> = withContext(Dispatchers.Unconfined) {
        val response = client.get(getUrlForLanguageId(languageId))
        when (response.status) {
            HttpStatusCode.OK -> Result.success(response.body())
            else -> Result.failure(IllegalStateException(response.body() as String))
        }
    }
}
