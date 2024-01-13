package com.bidyut.tech.seahorse.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.Operation
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.bidyut.tech.seahorse.Seahorse
import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

abstract class RefreshStringsWorker(
    context: Context,
    private val params: WorkerParameters,
) : CoroutineWorker(context, params) {
    abstract val seahorse: Seahorse

    override suspend fun doWork(): Result {
        val languages = params.inputData.getStringArray(KEY_LANGUAGES)?.map { it as LanguageId }
            ?: return Result.failure(
                workDataOf(
                    KEY_ERROR_MESSAGE to "languages are not provided",
                )
            )
        return withContext(Dispatchers.IO) {
            val successCount = seahorse.refreshStrings(languages)
            if (successCount == languages.size) {
                Result.success()
            } else {
                Result.failure(
                    workDataOf(
                        KEY_ERROR_MESSAGE to "failed to fetch ${languages.size - successCount} languages",
                    )
                )
            }
        }
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
