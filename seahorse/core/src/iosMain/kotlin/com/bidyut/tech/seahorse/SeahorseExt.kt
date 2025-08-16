@file:OptIn(ExperimentalObjCName::class)

package com.bidyut.tech.seahorse

import com.bidyut.tech.seahorse.model.LanguageId
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.datetime.toNSDate
import platform.BackgroundTasks.BGAppRefreshTaskRequest
import platform.BackgroundTasks.BGTaskScheduler
import kotlin.experimental.ExperimentalObjCName
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

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
        task?.setTaskCompletedWithSuccess(refreshStrings(languages) == languages.size)
        schedule(canRunImmediately)
    }
}

fun Seahorse.registerBackgroundTask(
    @ObjCName("_")
    languages: List<LanguageId>,
)  = registerBackgroundTask(languages, false)
