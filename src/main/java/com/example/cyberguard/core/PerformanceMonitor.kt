package com.example.cyberguard.core

import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object PerformanceMonitor {
    private const val TAG = "PerformanceMonitor"
    private const val ANR_THRESHOLD_MS = 4000L
    private val mainHandler = Handler(Looper.getMainLooper())
    fun <T> monitorOperation(operationName: String, operation: () -> T): T {
        val startTime = System.currentTimeMillis()
        return try {
            operation()
        } finally {
            val duration = System.currentTimeMillis() - startTime
            if (duration > ANR_THRESHOLD_MS) {
                Log.w(TAG, "Slow operation detected: $operationName took ${duration}ms")
            }
        }
    }
    fun executeOnBackground(
        operationName: String,
        backgroundOperation: suspend () -> Unit,
        onComplete: (() -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val startTime = System.currentTimeMillis()
            try {
                backgroundOperation()
            } catch (e: Exception) {
                Log.e(TAG, "Error in background operation: $operationName", e)
            } finally {
                val duration = System.currentTimeMillis() - startTime
                if (duration > ANR_THRESHOLD_MS) {
                    Log.w(TAG, "Slow background operation: $operationName took ${duration}ms")
                }
                onComplete?.let {
                    withContext(Dispatchers.Main) {
                        it()
                    }
                }
            }
        }
    }
    fun isMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }
    fun logThreadInfo(tag: String = TAG) {
        val isMain = isMainThread()
        val threadName = Thread.currentThread().name
        Log.d(tag, "Thread: $threadName, Main: $isMain")
    }
} 