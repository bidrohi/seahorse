package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import com.bidyut.tech.seahorse.utils.SeahorseUserAgentProvider
import com.bidyut.tech.seahorse.utils.UserAgentProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.userAgent
import okio.IOException

class OkHttpNetworkSource(
    private val client: OkHttpClient,
    private val getUrlForLanguageId: (LanguageId) -> String,
) : NetworkSource {
    constructor(
        getUrlForLanguageId: (LanguageId) -> String,
        userAgentProvider: UserAgentProvider = SeahorseUserAgentProvider(),
        config: OkHttpClient.Builder.() -> Unit,
    ) : this(
        OkHttpClient.Builder()
            .addNetworkInterceptor {
                it.proceed(
                    it.request()
                        .newBuilder()
                        .addHeader(
                            "User-Agent",
                            "${userAgentProvider.get()} $userAgent"
                        )
                        .build()
                )
            }
            .apply(config)
            .build(),
        getUrlForLanguageId,
    )

    constructor(
        getUrlForLanguageId: (LanguageId) -> String,
    ) : this(
        getUrlForLanguageId = getUrlForLanguageId,
        config = {},
    )

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun fetchStrings(
        languageId: LanguageId,
    ): Result<Map<String, String>> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(getUrlForLanguageId(languageId))
            .build()
        val call = client.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful && response.body != null) {
                Result.success(json.decodeFromStream(response.body!!.byteStream()))
            } else {
                Result.failure(IllegalStateException(response.message))
            }
        } catch (ex: IOException) {
            Result.failure(ex)
        }
    }
}
