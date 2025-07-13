package com.example.cyberguard.core

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.util.Log

object MemoryManager {
    private const val TAG = "MemoryManager"
    
    fun logMemoryInfo(context: Context) {
        try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            val availableMegs = memoryInfo.availMem / 1048576L
            val totalMegs = memoryInfo.totalMem / 1048576L
            val usedMegs = totalMegs - availableMegs
            Log.d(TAG, "Memory - Available: ${availableMegs}MB, Used: ${usedMegs}MB, Total: ${totalMegs}MB")
            if (memoryInfo.lowMemory) {
                Log.w(TAG, "Low memory detected!")
                System.gc()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting memory info", e)
        }
    }
    
    fun clearMemory() {
        try {
            System.gc()
            Log.d(TAG, "Memory cleared")
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing memory", e)
        }
    }
} 