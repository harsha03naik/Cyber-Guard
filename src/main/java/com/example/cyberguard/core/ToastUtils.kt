package com.example.cyberguard.core

import android.content.Context
import android.widget.Toast
import java.util.concurrent.ConcurrentHashMap

object ToastUtils {
    private val lastToastTime = ConcurrentHashMap<String, Long>()
    private const val TOAST_COOLDOWN_MS = 2000L
    fun showToast(context: Context?, message: String, duration: Int = Toast.LENGTH_SHORT) {
        context?.let { ctx ->
            val currentTime = System.currentTimeMillis()
            val lastTime = lastToastTime.getOrDefault(message, 0L)
            if (currentTime - lastTime > TOAST_COOLDOWN_MS) {
                Toast.makeText(ctx, message, duration).show()
                lastToastTime[message] = currentTime
            }
        }
    }
    fun showToast(context: Context?, message: String, tag: String, duration: Int = Toast.LENGTH_SHORT) {
        context?.let { ctx ->
            val currentTime = System.currentTimeMillis()
            val lastTime = lastToastTime.getOrDefault(tag, 0L)
            if (currentTime - lastTime > TOAST_COOLDOWN_MS) {
                Toast.makeText(ctx, message, duration).show()
                lastToastTime[tag] = currentTime
            }
        }
    }
} 