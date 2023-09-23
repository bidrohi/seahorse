package com.bidyut.tech.seahorse.example

import android.app.Application
import com.bidyut.tech.seahorse.example.di.AppGraph

class ExampleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppGraph.assign(AppGraph(this))
    }
}
