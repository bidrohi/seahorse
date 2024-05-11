package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO

class CioKtorNetworkSource(
    getUrlForLanguageId: (LanguageId) -> String,
    client: HttpClientConfig<*>.() -> Unit,
) : KtorNetworkSource(
    getUrlForLanguageId,
    CIO,
    client,
) {
    constructor(
        getUrlForLanguageId: (LanguageId) -> String,
    ) : this(
        getUrlForLanguageId,
        {},
    )
}
