package com.example.cyberguard.features.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cyberguard.R
import com.example.cyberguard.core.ToastUtils
import com.example.cyberguard.features.modules.ModuleViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class ProfileFragment : Fragment() {
    private var rootView: View? = null
    private lateinit var moduleViewModel: ModuleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize ViewModel
        moduleViewModel = ViewModelProvider(requireActivity())[ModuleViewModel::class.java]
        
        setupUI()
        observeProgress()
    }

    private fun setupUI() {
        rootView?.let { view ->
            // Setup buttons
            val shareProfileButton = view.findViewById<MaterialButton>(R.id.shareProfileButton)
            val resetProgressButton = view.findViewById<MaterialButton>(R.id.resetProgressButton)
            val exportProgressButton = view.findViewById<MaterialButton>(R.id.exportProgressButton)
            
            shareProfileButton.setOnClickListener {
                shareProgress()
            }
            
            resetProgressButton.setOnClickListener {
                resetProgress()
            }
            
            exportProgressButton.setOnClickListener {
                exportProgress()
            }
        }
    }

    private fun observeProgress() {
        moduleViewModel.modules.observe(viewLifecycleOwner) { modules ->
            updateProgressUI(modules)
        }
    }

    private fun updateProgressUI(modules: List<com.example.cyberguard.features.modules.model.LearningModule>) {
        rootView?.let { view ->
            val completedModules = modules.count { it.isCompleted }
            val totalModules = modules.size
            val unlockedModules = modules.count { it.isUnlocked }
            val averageProgress = if (totalModules > 0) modules.sumOf { it.progress } / totalModules else 0
            
            // Update progress text
            val completedModulesText = view.findViewById<TextView>(R.id.completedModulesText)
            val totalModulesText = view.findViewById<TextView>(R.id.totalModulesText)
            val unlockedModulesText = view.findViewById<TextView>(R.id.unlockedModulesText)
            val averageProgressText = view.findViewById<TextView>(R.id.averageProgressText)
            val overallProgressBar = view.findViewById<ProgressBar>(R.id.overallProgressBar)
            
            completedModulesText.text = "Completed: $completedModules"
            totalModulesText.text = "Total: $totalModules"
            unlockedModulesText.text = "Unlocked: $unlockedModules"
            averageProgressText.text = "Average Progress: ${averageProgress}%"
            overallProgressBar.progress = averageProgress
            
            // Update achievement section
            updateAchievements(modules)
            
            // Update recent activity
            updateRecentActivity(modules)
        }
    }

    private fun updateAchievements(modules: List<com.example.cyberguard.features.modules.model.LearningModule>) {
        rootView?.let { view ->
            val achievementsCard = view.findViewById<MaterialCardView>(R.id.achievementsCard)
            val achievementsText = view.findViewById<TextView>(R.id.achievementsText)
            
            val achievements = mutableListOf<String>()
            
            // Check for achievements
            val completedCount = modules.count { it.isCompleted }
            val totalModules = modules.size
            
            when {
                completedCount == totalModules -> achievements.add("🏆 Master - All modules completed!")
                completedCount >= totalModules * 0.8 -> achievements.add("⭐ Advanced - 80%+ modules completed")
                completedCount >= totalModules * 0.5 -> achievements.add("📚 Intermediate - 50%+ modules completed")
                completedCount >= totalModules * 0.2 -> achievements.add("🌱 Beginner - 20%+ modules completed")
                completedCount > 0 -> achievements.add("🚀 Started - First module completed")
                else -> achievements.add("🎯 Ready to start your cybersecurity journey!")
            }
            
            // Check for specific achievements
            if (modules.any { it.userRating >= 4.5f }) {
                achievements.add("⭐ High Rater - Rated modules highly")
            }
            
            if (modules.any { it.userNotes.isNotEmpty() }) {
                achievements.add("📝 Note Taker - Added personal notes")
            }
            
            achievementsText.text = achievements.joinToString("\n")
        }
    }

    private fun updateRecentActivity(modules: List<com.example.cyberguard.features.modules.model.LearningModule>) {
        rootView?.let { view ->
            val recentActivityText = view.findViewById<TextView>(R.id.recentActivityText)
            
            val recentModules = modules.filter { it.progress > 0 }
                .sortedByDescending { it.progress }
                .take(3)
            
            if (recentModules.isNotEmpty()) {
                val activityText = buildString {
                    appendLine("Recent Activity:")
                    recentModules.forEach { module ->
                        val status = when {
                            module.isCompleted -> "✅ Completed"
                            module.progress >= 75 -> "🔄 Almost Done (${module.progress}%)"
                            module.progress >= 50 -> "📖 In Progress (${module.progress}%)"
                            else -> "🟡 Started (${module.progress}%)"
                        }
                        appendLine("• ${module.title}: $status")
                    }
                }
                recentActivityText.text = activityText
            } else {
                recentActivityText.text = "No recent activity. Start your first module!"
            }
        }
    }

    private fun shareProgress() {
        val analytics = moduleViewModel.getProgressAnalytics()
        val completedModules = analytics["completedModules"] as Int
        val totalModules = analytics["totalModules"] as Int
        val completionPercentage = analytics["completionPercentage"] as Int
        
        val shareText = buildString {
            appendLine("🔒 CyberGuard Progress Report")
            appendLine("==========================")
            appendLine("Completed: $completedModules/$totalModules modules")
            appendLine("Progress: $completionPercentage%")
            appendLine()
            appendLine("I'm learning cybersecurity with CyberGuard!")
        }
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Share your progress"))
    }

    private fun resetProgress() {
        moduleViewModel.resetAllProgress()
        ToastUtils.showToast(context, "Progress reset successfully!", "reset_success")
    }

    private fun exportProgress() {
        val progressData = moduleViewModel.exportProgressData()
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, progressData)
            putExtra(Intent.EXTRA_SUBJECT, "CyberGuard Progress Report")
        }
        startActivity(Intent.createChooser(intent, "Export progress data"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }
} 