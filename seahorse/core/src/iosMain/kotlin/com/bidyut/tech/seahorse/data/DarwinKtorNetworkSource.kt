package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

class DarwinKtorNetworkSource(
    getUrlForLanguageId: (LanguageId) -> String,
    client: HttpClientConfig<*>.() -> Unit,
) : KtorNetworkSource(
    getUrlForLanguageId,
    Darwin,
    client,
) {
    constructor(
        getUrlForLanguageId: (LanguageId) -> String,
    ) : this(
        getUrlForLanguageId,
        {},
    )
}
