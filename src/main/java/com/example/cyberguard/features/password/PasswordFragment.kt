package com.example.cyberguard.features.password

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cyberguard.core.ToastUtils
import com.example.cyberguard.databinding.FragmentPasswordBinding
import java.security.SecureRandom
import android.graphics.Color
import android.view.animation.AnimationUtils

class PasswordFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    private val random = SecureRandom()
    private var currentGeneratedPassword: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)

        binding.btnCheckStrength.setOnClickListener {
            val input = binding.etPasswordInput.text.toString()
            if (TextUtils.isEmpty(input)) {
                ToastUtils.showToast(requireContext(), "Please enter a password", "password_empty")
            } else {
                binding.tvStrengthResult.text = checkPasswordStrength(input)
            }
        }

        binding.btnGeneratePassword.setOnClickListener {
            currentGeneratedPassword = generateRandomPassword(12)
            binding.tvGeneratedPassword.text = currentGeneratedPassword
            
            // Enable copy button
            binding.btnCopyPassword.isEnabled = true
            
            // Show success message
            ToastUtils.showToast(requireContext(), "Strong password generated! 🔐", "password_generated")
        }

        binding.btnCopyPassword.setOnClickListener {
            copyGeneratedPassword()
        }

        return binding.root
    }

    private fun copyGeneratedPassword() {
        try {
            if (currentGeneratedPassword.isEmpty()) {
                ToastUtils.showToast(requireContext(), "No password to copy. Generate one first!", "no_password")
                return
            }

            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Generated Password", currentGeneratedPassword)
            clipboard.setPrimaryClip(clip)
            
            // Visual feedback
            val copyButton = binding.btnCopyPassword
            // Animate button
            val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
            copyButton.startAnimation(scaleAnimation)
            
            // Change button text temporarily
            val originalText = copyButton.text
            copyButton.text = "✓ Copied!"
            copyButton.setBackgroundColor(Color.parseColor("#4CAF50")) // Green color
            
            // Reset button after 2 seconds
            copyButton.postDelayed({
                copyButton.text = originalText
                copyButton.setBackgroundColor(Color.parseColor("#2196F3")) // Original blue color
            }, 2000)
            
            // Show success message
            ToastUtils.showToast(requireContext(), "Password copied to clipboard! 📋", "password_copied")
            
        } catch (e: Exception) {
            // Handle clipboard errors
            ToastUtils.showToast(requireContext(), "Failed to copy password. Please try again.", "copy_error")
            android.util.Log.e("PasswordFragment", "Error copying password", e)
        }
    }

    private fun checkPasswordStrength(password: String): String {
        var score = 0
        if (password.length >= 12) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isLowerCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { "!@#\$%^&*()_+-=[]{}|;:,.<>?".contains(it) }) score++

        return when (score) {
            5 -> "Very Strong"
            4 -> "Strong"
            3 -> "Moderate"
            2 -> "Weak"
            else -> "Very Weak"
        }
    }

    private fun generateRandomPassword(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*()"
        return (1..length)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
