package com.bidyut.tech.seahorse.example.di

import android.annotation.SuppressLint
import android.content.Context
import com.bidyut.tech.seahorse.Seahorse
import com.bidyut.tech.seahorse.data.MapLocalSourceSink
import com.bidyut.tech.seahorse.data.MapNetworkSource
import com.bidyut.tech.seahorse.data.ResourceFallbackSource
import com.bidyut.tech.seahorse.model.LanguageBengali
import com.bidyut.tech.seahorse.model.LanguageEnglish

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
            networkSource = MapNetworkSource(
                mapOf(
                    LanguageEnglish to stringKeys.associateWith { "network $it" },
                    LanguageBengali to stringKeys.associateWith { "অন্তর্জাল $it" },
                )
            )
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
