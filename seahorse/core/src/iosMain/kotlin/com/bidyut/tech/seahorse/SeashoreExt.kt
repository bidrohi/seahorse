package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.toNSDate
import platform.BackgroundTasks.BGAppRefreshTaskRequest
import platform.BackgroundTasks.BGTaskScheduler
import platform.Foundation.NSDate
import platform.Foundation.NSTimeInterval
import kotlin.coroutines.cancellation.CancellationException

class FetchStringsFailureException(
    cause: Throwable? = null,
) : Exception("Failed to fetch strings", cause)

@Throws(CancellationException::class, FetchStringsFailureException::class)
suspend fun Seahorse.fetchStringsAsync(
    languageId: LanguageId,
): NSDate {
    val result = fetchStrings(languageId)
    return if (result.isSuccess) {
        result.getOrThrow().toNSDate()
    } else {
        throw FetchStringsFailureException(result.exceptionOrNull())
    }
}

fun Seahorse.refreshStrings(
    languages: List<LanguageId>,
): Boolean {
    return runBlocking {
        for (language in languages) {
            val result = fetchStrings(language)
            if (result.isFailure) {
                return@runBlocking false
            }
        }
        true
    }
}

fun Seahorse.getCacheInterval(): NSTimeInterval {
    return cacheInterval.inWholeSeconds.toDouble()
}

const val REFRESH_STRINGS_BACKGROUND_TASK_ID = "com.bidyut.tech.seahorse.refreshStrings"

fun Seahorse.schedule(): Boolean {
    val request = BGAppRefreshTaskRequest(REFRESH_STRINGS_BACKGROUND_TASK_ID)
    request.earliestBeginDate = (Clock.System.now() + cacheInterval).toNSDate()
    return BGTaskScheduler.sharedScheduler().submitTaskRequest(request, null)
}

fun Seahorse.registerBackgroundTask(
    languages: List<LanguageId>,
) {
    BGTaskScheduler.sharedScheduler().registerForTaskWithIdentifier(
        REFRESH_STRINGS_BACKGROUND_TASK_ID,
        null,
    ) { task ->
        task?.setTaskCompletedWithSuccess(refreshStrings(languages))
        schedule()
    }
}
