package com.example.cyberguard.core

import android.content.Context
import android.os.Process
import java.io.PrintWriter
import java.io.StringWriter

object CrashHandler : Thread.UncaughtExceptionHandler {
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context.applicationContext
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter)
            throwable.printStackTrace(printWriter)
            val stackTrace = stringWriter.toString()
            android.util.Log.e("CrashHandler", "App crashed: $stackTrace")
            ToastUtils.showToast(context, "Something went wrong. Please try again.", "crash_error")
        } catch (e: Exception) {
            android.util.Log.e("CrashHandler", "Error handling crash", e)
        } finally {
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
} 