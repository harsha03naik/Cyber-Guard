package com.example.cyberguard.features.modules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyberguard.R
import com.example.cyberguard.databinding.FragmentModuleBinding
import com.example.cyberguard.features.modules.model.LearningModule
import com.example.cyberguard.features.modules.ModuleAdapter
import com.example.cyberguard.features.modules.ModuleViewModel

class ModuleFragment : Fragment() {
    private var _binding: FragmentModuleBinding? = null
    private val binding get() = _binding!!
    private lateinit var moduleViewModel: ModuleViewModel
    private lateinit var moduleAdapter: ModuleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        try {
            setupViewModel()
            setupRecyclerView()
            observeModules()
            setupBackStackListener()
        } catch (e: Exception) {
            // Handle any initialization errors gracefully
            e.printStackTrace()
        }
    }

    private fun setupViewModel() {
        moduleViewModel = ViewModelProvider(requireActivity())[ModuleViewModel::class.java]
    }

    private fun setupRecyclerView() {
        binding.modulesRecyclerView.layoutManager = LinearLayoutManager(context)
        moduleAdapter = ModuleAdapter { module ->
            handleModuleClick(module)
        }
        binding.modulesRecyclerView.adapter = moduleAdapter
    }

    private fun handleModuleClick(module: LearningModule) {
        try {
            val index = moduleViewModel.modules.value?.indexOf(module) ?: -1
            if (index >= 0) {
                val detailFragment = ModuleDetailFragment.newInstance(index)
                
                // Hide RecyclerView and show fragment container
                binding.modulesRecyclerView.visibility = View.GONE
                binding.moduleFragmentContainer.visibility = View.VISIBLE
                
                // Clear any existing fragments in the container
                val existingFragment = parentFragmentManager.findFragmentById(R.id.moduleFragmentContainer)
                if (existingFragment != null) {
                    parentFragmentManager.beginTransaction()
                        .remove(existingFragment)
                        .commitAllowingStateLoss()
                }
                
                // Add the new fragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.moduleFragmentContainer, detailFragment)
                    .addToBackStack("module_detail")
                    .commitAllowingStateLoss()
                
                // Add debug logging
                android.util.Log.d("ModuleFragment", "Module clicked: ${module.title}, Index: $index")
            } else {
                android.util.Log.e("ModuleFragment", "Module not found in list")
            }
        } catch (e: Exception) {
            // Handle fragment transaction errors
            e.printStackTrace()
            android.util.Log.e("ModuleFragment", "Error handling module click", e)
        }
    }

    private fun observeModules() {
        moduleViewModel.modules.observe(viewLifecycleOwner) { modules ->
            try {
                android.util.Log.d("ModuleFragment", "Modules observed: ${modules.size} modules")
                moduleAdapter.submitList(modules)
                updateOverallProgress(modules)
                
                // Debug: Log first few modules
                modules.take(3).forEachIndexed { index, module ->
                    android.util.Log.d("ModuleFragment", "Module $index: ${module.title} (unlocked: ${module.isUnlocked})")
                }
            } catch (e: Exception) {
                // Handle adapter update errors
                e.printStackTrace()
                android.util.Log.e("ModuleFragment", "Error observing modules", e)
            }
        }
    }

    private fun setupBackStackListener() {
        parentFragmentManager.addOnBackStackChangedListener {
            try {
                if (parentFragmentManager.backStackEntryCount == 0) {
                    // Back to module list
                    binding.modulesRecyclerView.visibility = View.VISIBLE
                    binding.moduleFragmentContainer.visibility = View.GONE
                    android.util.Log.d("ModuleFragment", "Back to module list")
                }
            } catch (e: Exception) {
                // Handle back stack listener errors
                e.printStackTrace()
                android.util.Log.e("ModuleFragment", "Error in back stack listener", e)
            }
        }
    }

    private fun updateOverallProgress(modules: List<LearningModule>) {
        try {
            val completed = modules.count { it.isCompleted }
            val total = modules.size
            val progress = if (total > 0) (completed * 100 / total) else 0
            binding.progressBar.progress = progress
            binding.progressText.text = "Your Progress: $completed / $total"
        } catch (e: Exception) {
            // Handle progress update errors
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
