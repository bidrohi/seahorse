package com.bidyut.tech.seahorse.example.di

import com.bidyut.tech.seahorse.Seahorse
import com.bidyut.tech.seahorse.data.CioKtorNetworkSource
import com.bidyut.tech.seahorse.data.JdbcSqliteLocalStore
import com.bidyut.tech.seahorse.example.data.JsonResourceFallbackSource

class AppGraph {
    val stringKeys = listOf(
        "hello",
        "world",
        "platform",
        "sentence_structure",
        "foundation",
        "dune",
        "three_body_problem",
    )

    val seahorse: Seahorse by lazy {
        Seahorse {
            fallbackSource = JsonResourceFallbackSource()
            localStore = JdbcSqliteLocalStore()
            networkSource = CioKtorNetworkSource { languageId ->
                "https://www.bidyut.com/tech/seahorse/sample/${languageId.lowercase()}.json"
            }
        }
    }

    companion object {
        lateinit var instance: AppGraph
            private set

        val isAssigned: Boolean
            get() = ::instance.isInitialized

        fun assign(
            graph: AppGraph,
        ) {
            instance = graph
        }
    }
}
