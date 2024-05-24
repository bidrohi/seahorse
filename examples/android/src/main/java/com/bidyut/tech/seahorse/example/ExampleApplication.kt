package com.bidyut.tech.seahorse.example

import android.app.Application
import com.bidyut.tech.seahorse.example.di.AppGraph
import com.bidyut.tech.seahorse.example.worker.SeahorseRefreshWorker
import com.bidyut.tech.seahorse.model.LanguageBengali
import com.bidyut.tech.seahorse.model.LanguageEnglish
import com.bidyut.tech.seahorse.schedule

class ExampleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppGraph.assign(AppGraph(this))
        AppGraph.instance.seahorse.schedule(
            applicationContext,
            SeahorseRefreshWorker::class.java,
            arrayOf(LanguageEnglish, LanguageBengali),
        )
    }
}
