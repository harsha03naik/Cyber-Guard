package com.example.cyberguard.features.modules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieDrawable
import com.example.cyberguard.databinding.FragmentInteractiveModulesBinding

class InteractiveModulesFragment : Fragment() {
    private var _binding: FragmentInteractiveModulesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInteractiveModulesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            safeBrowsingAnimation.apply {
                setAnimation("safe_browsing.json")
                repeatCount = LottieDrawable.INFINITE
                playAnimation()
            }
            passwordSecurityAnimation.apply {
                setAnimation("password_security.json")
                repeatCount = LottieDrawable.INFINITE
                playAnimation()
            }
            twoFactorAuthAnimation.apply {
                setAnimation("two_factor_auth.json")
                repeatCount = LottieDrawable.INFINITE
                playAnimation()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}