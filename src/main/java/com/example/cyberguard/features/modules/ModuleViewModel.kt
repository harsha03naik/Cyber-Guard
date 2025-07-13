package com.example.cyberguard.features.modules

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cyberguard.core.PerformanceMonitor
import com.example.cyberguard.features.modules.model.LearningModule
import com.example.cyberguard.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModuleViewModel(application: Application) : AndroidViewModel(application) {
    private val _modules = MutableLiveData<List<LearningModule>>()
    val modules: LiveData<List<LearningModule>> = _modules
    private val prefs = application.getSharedPreferences("module_progress", Context.MODE_PRIVATE)
    init {
        viewModelScope.launch(Dispatchers.IO) {
            PerformanceMonitor.logThreadInfo("ModuleViewModel-init")
            val modulesList = createModulesList()
            loadSavedProgress(modulesList)
            withContext(Dispatchers.Main) {
                _modules.value = modulesList
                android.util.Log.d("ModuleViewModel", "Modules loaded: ${modulesList.size} modules")
                modulesList.forEachIndexed { index, module ->
                    android.util.Log.d("ModuleViewModel", "Module $index: ${module.title} (unlocked: ${module.isUnlocked}, progress: ${module.progress})")
                }
            }
        }
    }
    private fun loadSavedProgress(modules: List<LearningModule>): List<LearningModule> {
        return modules.map { module ->
            val moduleKey = module.title.replace(" ", "_").lowercase()
            val progress = prefs.getInt("${moduleKey}_progress", 0)
            val isCompleted = prefs.getBoolean("${moduleKey}_completed", false)
            val isUnlocked = prefs.getBoolean("${moduleKey}_unlocked", module.isUnlocked)
            val userNotes = prefs.getString("${moduleKey}_notes", "") ?: ""
            val userRating = prefs.getFloat("${moduleKey}_rating", 0f)
            val userFeedback = prefs.getString("${moduleKey}_feedback", "") ?: ""
            module.copy(
                progress = progress,
                isCompleted = isCompleted,
                isUnlocked = isUnlocked,
                userNotes = userNotes,
                userRating = userRating,
                userFeedback = userFeedback
            )
        }
    }
    private fun saveModuleProgress(module: LearningModule) {
        val moduleKey = module.title.replace(" ", "_").lowercase()
        prefs.edit().apply {
            putInt("${moduleKey}_progress", module.progress)
            putBoolean("${moduleKey}_completed", module.isCompleted)
            putBoolean("${moduleKey}_unlocked", module.isUnlocked)
            putString("${moduleKey}_notes", module.userNotes)
            putFloat("${moduleKey}_rating", module.userRating)
            putString("${moduleKey}_feedback", module.userFeedback)
        }.apply()
        android.util.Log.d("ModuleViewModel", "Saved progress for ${module.title}: ${module.progress}%")
    }
    private fun createModulesList(): List<LearningModule> {
        return PerformanceMonitor.monitorOperation("createModulesList") {
            listOf(
                LearningModule(
                    title = "Password Protection",
                    description = "Master the art of creating and managing strong, unique passwords. Learn about password managers, two-factor authentication, and best practices to keep your accounts secure.",
                    imageResId = R.drawable.ic_password_module,
                    videoUrl = "https://www.youtube.com/watch?v=-IPwDaUDnB4",
                    codeSample = "// Use a password manager\nval password = generateStrongPassword()\n// Enable 2FA\nval twoFactorEnabled = true",
                    isUnlocked = true,
                    progress = 0
                ),
                LearningModule(
                    title = "Secure Browsing",
                    description = "Navigate the web safely with HTTPS verification, ad blockers, and privacy tools. Learn to identify secure websites and protect your browsing data.",
                    imageResId = R.drawable.ic_browser_security,
                    videoUrl = "https://www.youtube.com/watch?v=bglWBr_rbM0",
                    codeSample = "// Always check for HTTPS\nval url = \"https://example.com\"\n// Use privacy extensions\nval privacyMode = true",
                    isUnlocked = false,
                    progress = 0
                ),
                LearningModule(
                    title = "Social Engineering Defense",
                    description = "Recognize and defend against social engineering attacks on social media, email, and phone calls. Learn to spot phishing attempts and protect your personal information.",
                    imageResId = R.drawable.ic_social_module,
                    videoUrl = "https://www.youtube.com/watch?v=uvKTMgWRPw4",
                    codeSample = "// Don't share personal info online!\n// Verify sender identity\nval suspiciousEmail = false",
                    isUnlocked = false,
                    progress = 0
                ),
                LearningModule(
                    title = "Wi-Fi Security",
                    description = "Secure your wireless networks and safely use public Wi-Fi. Learn about encryption protocols, VPN usage, and protecting your data on unsecured networks.",
                    imageResId = R.drawable.ic_wifi_security,
                    videoUrl = "https://www.youtube.com/watch?v=vn_GhGMJ5lk",
                    codeSample = "// Use WPA2 or WPA3 encryption\nval wifiSecure = true\n// Use VPN on public networks\nval vpnEnabled = true",
                    isUnlocked = false,
                    progress = 0
                ),
                LearningModule(
                    title = "Data Privacy",
                    description = "Manage your digital footprint and protect personal data. Learn about data minimization, privacy settings, and controlling what information you share online.",
                    imageResId = R.drawable.ic_data_privacy,
                    videoUrl = "https://www.youtube.com/watch?v=mMXdpXouSLo",
                    codeSample = "// Review app permissions regularly\n// Minimize data collection\nval privacyEnabled = true",
                    isUnlocked = false,
                    progress = 0
                ),
                LearningModule(
                    title = "Mobile Security",
                    description = "Secure your smartphone from mobile-specific threats. Learn about app permissions, device encryption, and protecting your mobile data from malware and theft.",
                    imageResId = R.drawable.ic_mobile_security,
                    videoUrl = "https://www.youtube.com/watch?v=6NGQ_Uq4bdM",
                    codeSample = "// Keep your OS and apps updated\n// Enable device encryption\nval mobileSecure = true",
                    isUnlocked = false,
                    progress = 0
                ),
                LearningModule(
                    title = "Malware Protection",
                    description = "Understand different types of malware and how to protect your devices. Learn about antivirus software, safe downloading practices, and recognizing malicious files.",
                    imageResId = R.drawable.ic_malware,
                    videoUrl = "https://www.youtube.com/watch?v=kjDbgMtPiSI",
                    codeSample = "// Use reputable antivirus software\n// Scan downloads before opening\nval malwareProtection = true",
                    isUnlocked = false,
                    progress = 0
                ),
                LearningModule(
                    title = "Ransomware Defense",
                    description = "Protect against ransomware attacks through regular backups, email security, and safe browsing habits. Learn to recognize and prevent ransomware threats.",
                    imageResId = R.drawable.ic_ransomware,
                    videoUrl = "https://www.youtube.com/watch?v=j-WmLpZAuPs",
                    codeSample = "// Regular backups are essential\n// Don't open suspicious attachments\nval backupEnabled = true",
                    isUnlocked = false,
                    progress = 0
                ),
                LearningModule(
                    title = "Phishing Awareness",
                    description = "Identify and avoid phishing attacks through email, text messages, and social media. Learn to spot red flags and verify suspicious communications.",
                    imageResId = R.drawable.ic_phishing,
                    videoUrl = "https://www.youtube.com/watch?v=aO858HyFbKI",
                    codeSample = "// Check sender email addresses\n// Don't click suspicious links\nval phishingAware = true",
                    isUnlocked = false,
                    progress = 0
                )
            )
        }
    }
    fun completeModule(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            PerformanceMonitor.logThreadInfo("ModuleViewModel-completeModule")
            val current = _modules.value?.toMutableList() ?: return@launch
            if (index in current.indices) {
                val updatedModule = current[index].copy(isCompleted = true, progress = 100)
                current[index] = updatedModule
                saveModuleProgress(updatedModule)
                if (index + 1 < current.size) {
                    val nextModule = current[index + 1].copy(isUnlocked = true)
                    current[index + 1] = nextModule
                    saveModuleProgress(nextModule)
                }
                if (current.all { it.isCompleted }) {
                    for (i in current.indices) {
                        val moduleWithBadge = current[i].copy(completionBadgeResId = com.example.cyberguard.R.drawable.ic_launcher_foreground)
                        current[i] = moduleWithBadge
                        saveModuleProgress(moduleWithBadge)
                    }
                }
                withContext(Dispatchers.Main) {
                    _modules.value = current
                }
            }
        }
    }
    fun allModulesCompleted(): Boolean {
        return _modules.value?.all { it.isCompleted } == true
    }
    fun updateModule(index: Int, notes: String? = null, rating: Float? = null, feedback: String? = null, progress: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            PerformanceMonitor.logThreadInfo("ModuleViewModel-updateModule")
            val current = _modules.value?.toMutableList() ?: return@launch
            if (index in current.indices) {
                var module = current[index]
                if (notes != null) module = module.copy(userNotes = notes)
                if (rating != null) module = module.copy(userRating = rating)
                if (feedback != null) module = module.copy(userFeedback = feedback)
                if (progress != null) {
                    val newProgress = minOf(progress, 100)
                    module = module.copy(progress = newProgress)
                    if (newProgress >= 100 && !module.isCompleted) {
                        module = module.copy(isCompleted = true)
                        if (index + 1 < current.size) {
                            current[index + 1] = current[index + 1].copy(isUnlocked = true)
                            saveModuleProgress(current[index + 1])
                        }
                    }
                }
                current[index] = module
                saveModuleProgress(module)
                withContext(Dispatchers.Main) {
                    _modules.value = current
                }
            }
        }
    }
    fun getProgressAnalytics(): Map<String, Any> {
        return PerformanceMonitor.monitorOperation("getProgressAnalytics") {
            val modules = _modules.value ?: emptyList()
            val totalModules = modules.size
            val completedModules = modules.count { it.isCompleted }
            val unlockedModules = modules.count { it.isUnlocked }
            val averageProgress = if (totalModules > 0) modules.sumOf { it.progress } / totalModules else 0
            mapOf(
                "totalModules" to totalModules,
                "completedModules" to completedModules,
                "unlockedModules" to unlockedModules,
                "averageProgress" to averageProgress,
                "completionPercentage" to if (totalModules > 0) (completedModules * 100 / totalModules) else 0
            )
        }
    }
    fun getAchievementBadge(moduleIndex: Int): Int? {
        val module = _modules.value?.getOrNull(moduleIndex) ?: return null
        return when {
            module.isCompleted -> com.example.cyberguard.R.drawable.ic_launcher_foreground
            module.progress >= 75 -> com.example.cyberguard.R.drawable.ic_launcher_foreground
            module.progress >= 50 -> com.example.cyberguard.R.drawable.ic_launcher_foreground
            else -> null
        }
    }
    fun resetAllProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            prefs.edit().clear().apply()
            val modulesList = createModulesList()
            withContext(Dispatchers.Main) {
                _modules.value = modulesList
            }
        }
    }
    fun exportProgressData(): String {
        val modules = _modules.value ?: emptyList()
        return buildString {
            appendLine("CyberGuard Progress Report")
            appendLine("=======================")
            appendLine()
            modules.forEach { module ->
                appendLine("Module: ${module.title}")
                appendLine("Progress: ${module.progress}%")
                appendLine("Completed: ${if (module.isCompleted) "Yes" else "No"}")
                appendLine("Unlocked: ${if (module.isUnlocked) "Yes" else "No"}")
                appendLine("Rating: ${module.userRating}/5")
                appendLine("Notes: ${module.userNotes}")
                appendLine("Feedback: ${module.userFeedback}")
                appendLine("---")
            }
        }
    }
} 