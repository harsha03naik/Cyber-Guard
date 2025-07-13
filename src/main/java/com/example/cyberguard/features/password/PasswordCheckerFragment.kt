package com.example.cyberguard.features.password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.cyberguard.databinding.FragmentPasswordCheckerBinding

class PasswordCheckerFragment : Fragment() {

    private var _binding: FragmentPasswordCheckerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordCheckerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.isEmpty()) {
                    binding.passwordInput.error = "Password cannot be empty"
                    binding.strengthText.text = "Very Weak"
                    binding.strengthProgress.progress = 0
                } else if (password.length < 8) {
                    binding.passwordInput.error = "Password must be at least 8 characters"
                    binding.strengthText.text = "Very Weak"
                    binding.strengthProgress.progress = 25
                } else {
                    binding.passwordInput.error = null
                    checkPasswordStrength(password)
                }
            }
        })
    }

    private fun checkPasswordStrength(password: String) {
        val strength = calculatePasswordStrength(password)
        binding.apply {
            when (strength) {
                0 -> {
                    strengthText.text = "Very Weak"
                    strengthText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
                    strengthProgress.progress = 25
                }
                1 -> {
                    strengthText.text = "Weak"
                    strengthText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark))
                    strengthProgress.progress = 50
                }
                2 -> {
                    strengthText.text = "Moderate"
                    strengthText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark))
                    strengthProgress.progress = 75
                }
                3 -> {
                    strengthText.text = "Strong"
                    strengthText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
                    strengthProgress.progress = 100
                }
            }
        }
    }

    private fun calculatePasswordStrength(password: String): Int {
        var strength = 0
        
        // Check length
        if (password.length >= 8) strength++
        if (password.length >= 12) strength++
        
        // Check complexity
        if (password.any { it.isDigit() }) strength++
        if (password.any { it.isLetter() }) strength++
        if (password.any { !it.isLetterOrDigit() }) strength++
        
        // Cap strength at 3
        return strength.coerceAtMost(3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}