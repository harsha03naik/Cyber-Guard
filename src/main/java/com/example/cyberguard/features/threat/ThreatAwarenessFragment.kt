package com.example.cyberguard.features.threat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyberguard.databinding.FragmentThreatAwarenessBinding
import com.example.cyberguard.R

class ThreatAwarenessFragment : Fragment() {
    private var _binding: FragmentThreatAwarenessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentThreatAwarenessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val threats = listOf(
            ThreatCard("Phishing Attacks", "Attempts to obtain sensitive info.", R.drawable.ic_phishing),
            ThreatCard("Malware", "Malicious software harming devices.", R.drawable.ic_malware),
            ThreatCard("Social Engineering", "Tricking users to reveal secrets.", R.drawable.ic_social_engineering),
            ThreatCard("Ransomware", "Encrypts files for ransom.", R.drawable.ic_ransomware)
        )
        
        binding.threatsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.threatsRecyclerView.adapter = ThreatCardAdapter(requireContext(), threats)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
