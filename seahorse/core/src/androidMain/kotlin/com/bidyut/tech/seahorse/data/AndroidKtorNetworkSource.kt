package com.bidyut.tech.seahorse.data

import com.bidyut.tech.seahorse.model.LanguageId
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.android.Android

class AndroidKtorNetworkSource(
    getUrlForLanguageId: (LanguageId) -> String,
    client: HttpClientConfig<*>.() -> Unit,
) : KtorNetworkSource(
    getUrlForLanguageId,
    Android,
    client,
) {
    constructor(
        getUrlForLanguageId: (LanguageId) -> String,
    ) : this(
        getUrlForLanguageId,
        {},
    )
}
