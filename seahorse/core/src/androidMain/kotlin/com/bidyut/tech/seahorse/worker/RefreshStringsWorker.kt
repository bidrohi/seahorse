package com.bidyut.tech.seahorse.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.Operation
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.bidyut.tech.seahorse.Seahorse
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

abstract class RefreshStringsWorker(
    context: Context,
    private val params: WorkerParameters,
) : Worker(context, params) {
    abstract val seahorse: Seahorse

    override fun doWork(): Result {
        val languages = params.inputData.getStringArray(KEY_LANGUAGES)
            ?: return Result.failure(
                workDataOf(
                    KEY_ERROR_MESSAGE to "languages is not provided",
                )
            )
        for (language in languages) {
            val result = runBlocking { seahorse.fetchStrings(language) }
            if (!result.isSuccess) {
                return Result.failure(
                    workDataOf(
                        KEY_ERROR_MESSAGE to "failed to fetch $language",
                    )
                )
            }
        }
        return Result.success()
    }

    companion object {
        const val NAME = "seahorse_refresh_strings"

        internal const val KEY_LANGUAGES = "languages"
        internal const val KEY_ERROR_MESSAGE = "error_message"

        fun schedule(
            workManager: WorkManager,
            workerClass: Class<out RefreshStringsWorker>,
            refreshInterval: Duration,
            languages: Array<String>,
        ): Operation {
            val request = PeriodicWorkRequest.Builder(
                workerClass,
                refreshInterval.inWholeMinutes,
                TimeUnit.MINUTES
            ).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).setInputData(
                workDataOf(
                    KEY_LANGUAGES to languages,
                )
            ).build()
            return workManager.enqueueUniquePeriodicWork(
                NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request,
            )
        }
    }
}
