package com.example.cyberguard

import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.example.cyberguard.core.CrashHandler
import com.example.cyberguard.core.EmergencyUtils
import com.example.cyberguard.core.MemoryManager
import com.example.cyberguard.core.ProgressTracker
import com.example.cyberguard.core.ToastUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var themeChangerBtn: ImageButton
    private lateinit var emergencyFab: FloatingActionButton
    private lateinit var progressTracker: ProgressTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CrashHandler.init(this)
        progressTracker = ProgressTracker(this)
        try {
            setContentView(R.layout.activity_main)
            initializeViews()
            setupViewPager()
            setupThemeButton()
            setupEmergencyButton()
            progressTracker.startSession()
            progressTracker.updateStreak()
            MemoryManager.logMemoryInfo(this)
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showToast(this, "Error initializing app", "init_error")
        }
    }

    private fun initializeViews() {
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        themeChangerBtn = findViewById(R.id.themeChangerBtn)
        emergencyFab = findViewById(R.id.emergencyFab)
    }

    private fun setupViewPager() {
        try {
            val pagerAdapter = SectionsPagerAdapter(this)
            viewPager.adapter = pagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Threats"
                    1 -> "Modules"
                    2 -> "Quiz"
                    3 -> "Password"
                    else -> "Unknown"
                }
            }.attach()
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    progressTracker.updateActivity()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupThemeButton() {
        try {
            themeChangerBtn.setOnClickListener {
                toggleTheme()
                progressTracker.updateActivity()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupEmergencyButton() {
        try {
            emergencyFab.setOnClickListener {
                android.util.Log.d("MainActivity", "Emergency button clicked")
                progressTracker.updateActivity()
                EmergencyUtils.openCybercrimePortal(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("MainActivity", "Error setting up emergency button", e)
        }
    }

    private fun toggleTheme() {
        try {
            val currentMode = AppCompatDelegate.getDefaultNightMode()
            val newMode = when (currentMode) {
                AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else -> AppCompatDelegate.MODE_NIGHT_YES
            }
            getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("theme_mode", newMode)
                .apply()
            viewPager.post {
                AppCompatDelegate.setDefaultNightMode(newMode)
                val themeName = when (newMode) {
                    AppCompatDelegate.MODE_NIGHT_YES -> "Dark"
                    AppCompatDelegate.MODE_NIGHT_NO -> "Light"
                    else -> "System"
                }
                ToastUtils.showToast(this, "Theme changed to $themeName", "theme_change")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showToast(this, "Error changing theme", "theme_error")
        }
    }
    
    override fun onResume() {
        super.onResume()
        progressTracker.updateActivity()
    }
    
    override fun onPause() {
        super.onPause()
        progressTracker.updateActivity()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        progressTracker.endSession()
        MemoryManager.clearMemory()
    }
} 