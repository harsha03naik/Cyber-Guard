package com.example.cyberguard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cyberguard.features.threat.ThreatAwarenessFragment
import com.example.cyberguard.features.modules.ModuleFragment
import com.example.cyberguard.features.quiz.QuizFragment
import com.example.cyberguard.features.password.PasswordFragment

class SectionsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 4
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ThreatAwarenessFragment()
            1 -> ModuleFragment()
            2 -> QuizFragment()
            3 -> PasswordFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
