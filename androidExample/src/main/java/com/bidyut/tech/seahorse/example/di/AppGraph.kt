package com.bidyut.tech.seahorse.example.di

import android.annotation.SuppressLint
import android.content.Context
import com.bidyut.tech.seahorse.Seahorse
import com.bidyut.tech.seahorse.data.AndroidKtorNetworkSource
import com.bidyut.tech.seahorse.data.MapLocalSourceSink
import com.bidyut.tech.seahorse.data.ResourceFallbackSource

class AppGraph(
    val context: Context
) {
    val stringKeys = listOf(
        "hello",
        "world",
        "foundation",
        "dune",
        "three_body_problem",
    )

    val seahorse: Seahorse by lazy {
        Seahorse {
            fallbackSource = ResourceFallbackSource(context)
            val sourceSink = MapLocalSourceSink()
            localSource = sourceSink
            localSink = sourceSink
            networkSource = AndroidKtorNetworkSource { languageId ->
                "https://www.bidyut.com/tech/seahorse/sample/${languageId.lowercase()}.json"
            }
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: AppGraph
            private set

        fun assign(
            graph: AppGraph,
        ) {
            instance = graph
        }
    }
}
