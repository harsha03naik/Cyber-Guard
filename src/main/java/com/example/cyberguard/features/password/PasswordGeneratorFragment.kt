package com.example.cyberguard.features.password

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cyberguard.R
import com.example.cyberguard.core.ToastUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlin.random.Random
import android.animation.ObjectAnimator
import android.graphics.Color
import android.view.animation.AnimationUtils

class PasswordGeneratorFragment : Fragment() {
    private var rootView: View? = null
    private var currentPassword: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_password_generator, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lengthInput = view.findViewById<EditText>(R.id.lengthInput)
        val uppercaseSwitch = view.findViewById<SwitchMaterial>(R.id.uppercaseSwitch)
        val lowercaseSwitch = view.findViewById<SwitchMaterial>(R.id.lowercaseSwitch)
        val digitsSwitch = view.findViewById<SwitchMaterial>(R.id.digitsSwitch)
        val symbolsSwitch = view.findViewById<SwitchMaterial>(R.id.symbolsSwitch)
        val generateButton = view.findViewById<MaterialButton>(R.id.generateButton)
        val generatedPassword = view.findViewById<TextView>(R.id.generatedPassword)
        val copyButton = view.findViewById<MaterialButton>(R.id.copyButton)

        // Initially disable copy button until password is generated
        copyButton.isEnabled = false
        copyButton.alpha = 0.5f

        generateButton.setOnClickListener {
            val length = lengthInput.text.toString().toIntOrNull() ?: 12
            val useUpper = uppercaseSwitch.isChecked
            val useLower = lowercaseSwitch.isChecked
            val useDigits = digitsSwitch.isChecked
            val useSymbols = symbolsSwitch.isChecked
            
            // Validate at least one option is selected
            if (!useUpper && !useLower && !useDigits && !useSymbols) {
                ToastUtils.showToast(requireContext(), "Please select at least one character type", "validation_error")
                return@setOnClickListener
            }
            
            currentPassword = generatePassword(length, useUpper, useLower, useDigits, useSymbols)
            generatedPassword.text = currentPassword
            
            // Enable copy button
            copyButton.isEnabled = true
            copyButton.alpha = 1.0f
            
            // Animate generated password
            ObjectAnimator.ofFloat(generatedPassword, "alpha", 0f, 1f).setDuration(500).start()
            
            // Show success message
            ToastUtils.showToast(requireContext(), "Strong password generated! 🔐", "password_generated")
        }
        
        copyButton.setOnClickListener {
            copyPasswordToClipboard()
        }
    }

    private fun copyPasswordToClipboard() {
        try {
            if (currentPassword.isEmpty()) {
                ToastUtils.showToast(requireContext(), "No password to copy. Generate one first!", "no_password")
                return
            }

            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Generated Password", currentPassword)
            clipboard.setPrimaryClip(clip)
            
            // Visual feedback
            val copyButton = rootView?.findViewById<MaterialButton>(R.id.copyButton)
            copyButton?.let { button ->
                // Animate button
                val scaleAnimation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
                button.startAnimation(scaleAnimation)
                
                // Change button text temporarily
                val originalText = button.text
                button.text = "✓ Copied!"
                button.setBackgroundColor(Color.parseColor("#4CAF50")) // Green color
                
                // Reset button after 2 seconds
                button.postDelayed({
                    button.text = originalText
                    button.setBackgroundColor(Color.parseColor("#2196F3")) // Original blue color
                }, 2000)
            }
            
            // Show success message
            ToastUtils.showToast(requireContext(), "Password copied to clipboard! 📋", "password_copied")
            
        } catch (e: Exception) {
            // Handle clipboard errors
            ToastUtils.showToast(requireContext(), "Failed to copy password. Please try again.", "copy_error")
            android.util.Log.e("PasswordGenerator", "Error copying password", e)
        }
    }

    private fun generatePassword(length: Int, upper: Boolean, lower: Boolean, digits: Boolean, symbols: Boolean): String {
        val upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val lowerChars = "abcdefghijklmnopqrstuvwxyz"
        val digitChars = "0123456789"
        val symbolChars = "!@#${'$'}%^&*()-_=+[]{};:,.<>?/"
        
        var chars = ""
        if (upper) chars += upperChars
        if (lower) chars += lowerChars
        if (digits) chars += digitChars
        if (symbols) chars += symbolChars
        
        // Ensure at least one character type is selected
        if (chars.isEmpty()) {
            chars = upperChars + lowerChars + digitChars
        }
        
        // Generate password with at least one character from each selected type
        val password = StringBuilder()
        
        // Add at least one character from each selected type
        if (upper) password.append(upperChars[Random.nextInt(upperChars.length)])
        if (lower) password.append(lowerChars[Random.nextInt(lowerChars.length)])
        if (digits) password.append(digitChars[Random.nextInt(digitChars.length)])
        if (symbols) password.append(symbolChars[Random.nextInt(symbolChars.length)])
        
        // Fill the rest with random characters
        while (password.length < length) {
            password.append(chars[Random.nextInt(chars.length)])
        }
        
        // Shuffle the password to ensure randomness
        return password.toString().toList().shuffled().joinToString("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }
} 