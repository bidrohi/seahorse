package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.winhttp.WinHttp

class WinHttpKtorNetworkSource(
    getUrlForLanguageId: (LanguageId) -> String,
    client: HttpClientConfig<*>.() -> Unit,
) : KtorNetworkSource(
    getUrlForLanguageId,
    WinHttp,
    client,
) {
    constructor(
        getUrlForLanguageId: (LanguageId) -> String,
    ) : this(
        getUrlForLanguageId,
        {},
    )
}
