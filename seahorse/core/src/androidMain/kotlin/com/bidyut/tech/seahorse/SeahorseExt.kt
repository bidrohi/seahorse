package com.bidyut.tech.seahorse

import android.content.Context
import androidx.work.WorkManager
import com.bidyut.tech.seahorse.worker.RefreshStringsWorker

fun Seahorse.schedule(
    workManager: WorkManager,
    workerClass: Class<out RefreshStringsWorker>,
    languages: Array<String>,
) {
    RefreshStringsWorker.schedule(
        workManager,
        workerClass,
        cacheInterval,
        languages,
    )
}

fun Seahorse.schedule(
    context: Context,
    workerClass: Class<out RefreshStringsWorker>,
    languages: Array<String>,
) {
    schedule(
        WorkManager.getInstance(context),
        workerClass,
        languages,
    )
}
