package com.bidyut.tech.seahorse.worker

import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.Operation
import androidx.work.WorkerParameters
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import com.bidyut.tech.seahorse.Seahorse
import com.bidyut.tech.seahorse.schedule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.versioning.AndroidVersions
import kotlin.test.Test
import kotlin.test.assertIs

@Config(sdk = [AndroidVersions.N.SDK_INT])
@RunWith(RobolectricTestRunner::class)
class RefreshStringsWorkerTest {
    @Test
    fun `ensure worker runs and fetches remote strings`() {
        val context = RuntimeEnvironment.getApplication() as Context
        val languages = arrayOf("en", "bn")
        val worker = TestListenableWorkerBuilder<TestRefreshStringsWorker>(context)
            .setInputData(
                workDataOf(RefreshStringsWorker.KEY_LANGUAGES to languages)
            )
            .build()

        runBlocking {
            val result = worker.doWork()
            assertIs<ListenableWorker.Result.Failure>(result)
            verify(exactly = 1) {
                worker.seahorse.refreshStrings(any())
            }
        }

        every {
            worker.seahorse.refreshStrings(any())
        } returns languages.size

        runBlocking {
            val result = worker.doWork()
            assertIs<ListenableWorker.Result.Success>(result)
            verify(exactly = 2) {
                worker.seahorse.refreshStrings(any())
            }
        }
    }

    @Test
    fun `ensure worker fails when languages are not provided`() {
        val context = RuntimeEnvironment.getApplication() as Context
        val worker = TestListenableWorkerBuilder<TestRefreshStringsWorker>(context)
            .build()

        runBlocking {
            val result = worker.doWork()
            assertIs<ListenableWorker.Result.Failure>(result)
        }
    }

    @Test
    fun `ensure worker is scheduled`() {
        val context = RuntimeEnvironment.getApplication() as Context
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
        val seahorse = mockk<Seahorse>(relaxed = true)

        val op = seahorse.schedule(
            context = context,
            workerClass = TestRefreshStringsWorker::class.java,
            languages = arrayOf("en", "bn"),
        )
        assertIs<Operation.State.SUCCESS>(op.result.get())
    }

    class TestRefreshStringsWorker(
        context: Context,
        workerParams: WorkerParameters,
    ) : RefreshStringsWorker(context, workerParams) {
        override val seahorse: Seahorse = mockk(relaxed = true)
    }
}
