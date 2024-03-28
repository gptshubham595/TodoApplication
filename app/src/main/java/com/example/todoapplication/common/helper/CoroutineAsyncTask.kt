package com.example.todoapplication.common.helper

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.runInterruptible

abstract class CoroutineAsyncTask<Params, Progress, Result> {

    enum class Status {
        PENDING,
        RUNNING,
        FINISHED
    }
    private val scope = CoroutineScope(Dispatchers.IO) + Job()

    private val _status = AtomicReference<Status>(Status.PENDING)
    val status get() = _status.get()!!

    private val _isCancelled = AtomicBoolean(false)
    val isCancelled get() = _isCancelled.get()!!

    private val executionResult = AtomicReference<Result>()
    private val executionJob = AtomicReference<Job?>()
    private val executionException = AtomicReference<Throwable?>()

    @WorkerThread
    protected abstract fun doInBackground(vararg params: Params): Result

    @MainThread
    open fun onPreExecute() {
    }

    @MainThread
    open fun onPostExecute(result: Result?) {
    }

    @MainThread
    fun execute(vararg params: Params): CoroutineAsyncTask<Params, Progress, Result> {
        when (status) {
            Status.PENDING -> {
                _status.set(Status.RUNNING)
            }
            Status.RUNNING -> {
                throw IllegalStateException("Cannot execute task: the task is already running.")
            }
            Status.FINISHED -> {
                throw IllegalStateException("Already Finished!")
            }
        }
        onPreExecute()
        val job = scope.launch {
            try {
                if (!isCancelled) {
                    runInterruptible {
                        val result = doInBackground(*params)
                        executionResult.set(result)
                    }
                } else {
                    throw CancellationException()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _isCancelled.set(true)
                executionException.set(e)
            } finally {
                _status.set(Status.FINISHED)
                onPostExecute(executionResult.get())
            }
        }

        executionJob.set(job)

        return this
    }

    fun cancel() {
        val j = executionJob.get()
        j?.let {
            if (j.isCompleted || j.isCancelled) {
                return
            } else {
                j.cancel()
                _isCancelled.set(true)
            }
        }
    }
}
