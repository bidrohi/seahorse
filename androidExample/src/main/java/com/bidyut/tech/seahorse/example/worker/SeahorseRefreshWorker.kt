package com.bidyut.tech.seahorse.example.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.bidyut.tech.seahorse.Seahorse
import com.bidyut.tech.seahorse.example.di.AppGraph
import com.bidyut.tech.seahorse.worker.RefreshStringsWorker

class SeahorseRefreshWorker(
    context: Context,
    params: WorkerParameters,
) : RefreshStringsWorker(context, params) {
    override val seahorse: Seahorse
        get() = AppGraph.instance.seahorse
}
