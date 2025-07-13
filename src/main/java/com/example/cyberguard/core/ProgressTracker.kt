package com.example.cyberguard.core

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.util.*

class ProgressTracker(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("progress_tracker", Context.MODE_PRIVATE)
    companion object {
        private const val TAG = "ProgressTracker"
        private const val KEY_LAST_ACTIVITY = "last_activity"
        private const val KEY_SESSION_START = "session_start"
        private const val KEY_TOTAL_SESSIONS = "total_sessions"
        private const val KEY_TOTAL_TIME = "total_time"
        private const val KEY_STREAK_DAYS = "streak_days"
        private const val KEY_LAST_LOGIN = "last_login"
    }
    fun startSession() {
        val currentTime = System.currentTimeMillis()
        prefs.edit().apply {
            putLong(KEY_SESSION_START, currentTime)
            putLong(KEY_LAST_ACTIVITY, currentTime)
        }.apply()
        Log.d(TAG, "Session started at ${Date(currentTime)}")
    }
    fun updateActivity() {
        val currentTime = System.currentTimeMillis()
        prefs.edit().putLong(KEY_LAST_ACTIVITY, currentTime).apply()
    }
    fun endSession() {
        val sessionStart = prefs.getLong(KEY_SESSION_START, 0)
        val currentTime = System.currentTimeMillis()
        if (sessionStart > 0) {
            val sessionDuration = currentTime - sessionStart
            val totalTime = prefs.getLong(KEY_TOTAL_TIME, 0) + sessionDuration
            val totalSessions = prefs.getInt(KEY_TOTAL_SESSIONS, 0) + 1
            prefs.edit().apply {
                putLong(KEY_TOTAL_TIME, totalTime)
                putInt(KEY_TOTAL_SESSIONS, totalSessions)
            }.apply()
            Log.d(TAG, "Session ended. Duration: ${sessionDuration / 1000}s, Total time: ${totalTime / 1000}s")
        }
    }
    fun updateStreak() {
        val lastLogin = prefs.getLong(KEY_LAST_LOGIN, 0)
        val currentTime = System.currentTimeMillis()
        val currentDay = currentTime / (24 * 60 * 60 * 1000)
        val lastLoginDay = lastLogin / (24 * 60 * 60 * 1000)
        val currentStreak = prefs.getInt(KEY_STREAK_DAYS, 0)
        when {
            lastLogin == 0L -> {
                prefs.edit().apply {
                    putLong(KEY_LAST_LOGIN, currentTime)
                    putInt(KEY_STREAK_DAYS, 1)
                }.apply()
            }
            currentDay == lastLoginDay -> {
            }
            currentDay == lastLoginDay + 1 -> {
                prefs.edit().apply {
                    putLong(KEY_LAST_LOGIN, currentTime)
                    putInt(KEY_STREAK_DAYS, currentStreak + 1)
                }.apply()
            }
            else -> {
                prefs.edit().apply {
                    putLong(KEY_LAST_LOGIN, currentTime)
                    putInt(KEY_STREAK_DAYS, 1)
                }.apply()
            }
        }
    }
    fun getSessionStats(): Map<String, Any> {
        val totalSessions = prefs.getInt(KEY_TOTAL_SESSIONS, 0)
        val totalTime = prefs.getLong(KEY_TOTAL_TIME, 0)
        val streakDays = prefs.getInt(KEY_STREAK_DAYS, 0)
        val lastActivity = prefs.getLong(KEY_LAST_ACTIVITY, 0)
        return mapOf(
            "totalSessions" to totalSessions,
            "totalTimeMinutes" to (totalTime / (60 * 1000)),
            "streakDays" to streakDays,
            "lastActivity" to lastActivity,
            "averageSessionMinutes" to if (totalSessions > 0) (totalTime / (60 * 1000)) / totalSessions else 0
        )
    }
    fun getActivitySummary(): String {
        val stats = getSessionStats()
        val totalSessions = stats["totalSessions"] as Int
        val totalTimeMinutes = stats["totalTimeMinutes"] as Long
        val streakDays = stats["streakDays"] as Int
        val averageSessionMinutes = stats["averageSessionMinutes"] as Long
        return buildString {
            appendLine("📊 Activity Summary")
            appendLine("==================")
            appendLine("Total Sessions: $totalSessions")
            appendLine("Total Time: ${totalTimeMinutes} minutes")
            appendLine("Current Streak: $streakDays days")
            appendLine("Average Session: ${averageSessionMinutes} minutes")
        }
    }
    fun resetStats() {
        prefs.edit().clear().apply()
        Log.d(TAG, "All progress statistics reset")
    }
} 