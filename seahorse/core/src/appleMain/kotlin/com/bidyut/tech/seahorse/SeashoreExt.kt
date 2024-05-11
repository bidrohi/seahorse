@file:OptIn(ExperimentalObjCName::class)

package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.datetime.Clock
import kotlinx.datetime.toNSDate
import platform.BackgroundTasks.BGAppRefreshTaskRequest
import platform.BackgroundTasks.BGTaskScheduler
import platform.Foundation.NSDate
import platform.Foundation.NSTimeInterval
import kotlin.coroutines.cancellation.CancellationException
import kotlin.experimental.ExperimentalObjCName
import kotlin.time.Duration.Companion.seconds

fun Seahorse.getStringForLanguage(
    @ObjCName("_")
    languageId: LanguageId,
    @ObjCName("_")
    key: String,
    args: List<Any>,
): String = getStringForLanguage(languageId, key, *args.toTypedArray())

fun Seahorse.getString(
    @ObjCName("_")
    key: String,
    args: List<Any>,
): String = getStringForLanguage(defaultLanguageId, key, *args.toTypedArray())

class FetchStringsFailureException(
    cause: Throwable? = null,
) : Exception("Failed to fetch strings", cause)

@Throws(CancellationException::class, FetchStringsFailureException::class)
suspend fun Seahorse.fetchStringsAsync(
    @ObjCName("_")
    languageId: LanguageId,
): NSDate {
    val result = fetchStrings(languageId)
    return if (result.isSuccess) {
        result.getOrThrow().toNSDate()
    } else {
        throw FetchStringsFailureException(result.exceptionOrNull())
    }
}

fun Seahorse.getCacheInterval(): NSTimeInterval {
    return cacheInterval.inWholeSeconds.toDouble()
}

const val REFRESH_STRINGS_BACKGROUND_TASK_ID = "com.bidyut.tech.seahorse.refreshStrings"

@OptIn(ExperimentalForeignApi::class)
fun Seahorse.schedule(
    canRunImmediately: Boolean,
): Boolean {
    val request = BGAppRefreshTaskRequest(REFRESH_STRINGS_BACKGROUND_TASK_ID)
    val runDelay = if (canRunImmediately) 30.seconds else cacheInterval
    request.earliestBeginDate = (Clock.System.now() + runDelay).toNSDate()
    return BGTaskScheduler.sharedScheduler().submitTaskRequest(request, null)
}

fun Seahorse.schedule() = schedule(false)

fun Seahorse.registerBackgroundTask(
    @ObjCName("_")
    languages: List<LanguageId>,
    canRunImmediately: Boolean,
) {
    BGTaskScheduler.sharedScheduler().registerForTaskWithIdentifier(
        REFRESH_STRINGS_BACKGROUND_TASK_ID,
        null,
    ) { task ->
        task?.setTaskCompletedWithSuccess(refreshStrings(languages))
        schedule(canRunImmediately)
    }
}

fun Seahorse.registerBackgroundTask(
    @ObjCName("_")
    languages: List<LanguageId>,
)  = registerBackgroundTask(languages, false)
