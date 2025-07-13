package com.example.cyberguard.features.modules

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cyberguard.R
import com.example.cyberguard.core.ToastUtils
import com.example.cyberguard.databinding.FragmentModuleDetailBinding
import com.example.cyberguard.features.modules.model.LearningModule

class ModuleDetailFragment : Fragment() {

    private var _binding: FragmentModuleDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var moduleViewModel: ModuleViewModel
    private var moduleIndex: Int = -1
    private var currentModule: LearningModule? = null

    companion object {
        private const val ARG_MODULE_INDEX = "module_index"

        fun newInstance(moduleIndex: Int): ModuleDetailFragment {
            return ModuleDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MODULE_INDEX, moduleIndex)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            _binding = FragmentModuleDetailBinding.inflate(inflater, container, false)
            android.util.Log.d("ModuleDetailFragment", "View created successfully")
            return binding.root
        } catch (e: Exception) {
            android.util.Log.e("ModuleDetailFragment", "Error creating view", e)
            throw e
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        try {
            android.util.Log.d("ModuleDetailFragment", "onViewCreated called")
            setupViewModel()
            loadModuleData()
            setupUI()
            setupVideoPlayer()
            setupInteractiveElements()
            android.util.Log.d("ModuleDetailFragment", "Module detail setup completed")
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("ModuleDetailFragment", "Error in onViewCreated", e)
            ToastUtils.showToast(context, "Error loading module", "module_error")
        }
    }

    private fun setupViewModel() {
        moduleViewModel = ViewModelProvider(requireActivity())[ModuleViewModel::class.java]
    }

    private fun loadModuleData() {
        moduleIndex = arguments?.getInt(ARG_MODULE_INDEX, -1) ?: -1
        android.util.Log.d("ModuleDetailFragment", "Loading module data for index: $moduleIndex")
        if (moduleIndex >= 0) {
            currentModule = moduleViewModel.modules.value?.getOrNull(moduleIndex)
            android.util.Log.d("ModuleDetailFragment", "Current module: ${currentModule?.title}")
        } else {
            android.util.Log.e("ModuleDetailFragment", "Invalid module index: $moduleIndex")
        }
    }

    private fun setupUI() {
        currentModule?.let { module ->
            android.util.Log.d("ModuleDetailFragment", "Setting up UI for module: ${module.title}")
            
            // Set title and description
            binding.moduleTitle.text = module.title
            binding.moduleDescription.text = module.description

            // Set progress
            binding.moduleProgressBar.progress = module.progress
            binding.progressText.text = "${module.progress}% Complete"

            // Set code sample
            binding.codeSample.text = module.codeSample ?: "// No code sample available"

            // Set completion badge if completed
            if (module.isCompleted) {
                binding.completionBadge.visibility = View.VISIBLE
                binding.completionBadge.setImageResource(
                    module.completionBadgeResId ?: R.drawable.ic_launcher_foreground
                )
            }

            // Set existing user data
            binding.userNotes.setText(module.userNotes)
            binding.userFeedback.setText(module.userFeedback)
            binding.moduleRating.rating = module.userRating

            // Update button states
            updateButtonStates(module)
            
            android.util.Log.d("ModuleDetailFragment", "UI setup completed for module: ${module.title}")
        } ?: run {
            android.util.Log.e("ModuleDetailFragment", "Current module is null, cannot setup UI")
        }

        // Setup back button
        binding.backButton.setOnClickListener {
            android.util.Log.d("ModuleDetailFragment", "Back button clicked")
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        
        // Add a test log to verify the fragment is loaded
        android.util.Log.d("ModuleDetailFragment", "Fragment loaded successfully with module: ${currentModule?.title}")
    }

    private fun setupVideoPlayer() {
        currentModule?.let { module ->
            android.util.Log.d("ModuleDetailFragment", "Setting up video player for module: ${module.title}")
            android.util.Log.d("ModuleDetailFragment", "Video URL: ${module.videoUrl}")
            
            if (!module.videoUrl.isNullOrEmpty()) {
                binding.videoWebView.settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    mediaPlaybackRequiresUserGesture = false
                    allowContentAccess = true
                    allowFileAccess = true
                    mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }

                binding.videoWebView.webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        android.util.Log.d("ModuleDetailFragment", "Video page started loading: $url")
                    }
                    
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        android.util.Log.d("ModuleDetailFragment", "Video page finished loading: $url")
                    }
                    
                    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                        super.onReceivedError(view, errorCode, description, failingUrl)
                        android.util.Log.e("ModuleDetailFragment", "Video loading error: $errorCode - $description for URL: $failingUrl")
                        ToastUtils.showToast(context, "Video loading failed. Please check your internet connection.", "video_error")
                    }
                }

                // Convert YouTube URL to embed URL
                val embedUrl = convertToEmbedUrl(module.videoUrl)
                android.util.Log.d("ModuleDetailFragment", "Loading embed URL: $embedUrl")
                binding.videoWebView.loadUrl(embedUrl)
                binding.videoDescription.text = "Watch this video to learn about ${module.title}"
                
                // Show video section
                binding.videoSection.visibility = View.VISIBLE
            } else {
                android.util.Log.d("ModuleDetailFragment", "No video URL provided for module: ${module.title}")
                binding.videoSection.visibility = View.GONE
            }
        } ?: run {
            android.util.Log.e("ModuleDetailFragment", "Current module is null, cannot setup video player")
            binding.videoSection.visibility = View.GONE
        }
    }

    private fun convertToEmbedUrl(url: String): String {
        return when {
            url.contains("youtube.com/watch") -> {
                val videoId = url.substringAfter("v=").substringBefore("&")
                val embedUrl = "https://www.youtube.com/embed/$videoId?rel=0&showinfo=0"
                android.util.Log.d("ModuleDetailFragment", "Converted YouTube URL: $url -> $embedUrl")
                embedUrl
            }
            url.contains("youtu.be/") -> {
                val videoId = url.substringAfter("youtu.be/")
                val embedUrl = "https://www.youtube.com/embed/$videoId?rel=0&showinfo=0"
                android.util.Log.d("ModuleDetailFragment", "Converted YouTube URL: $url -> $embedUrl")
                embedUrl
            }
            else -> {
                android.util.Log.d("ModuleDetailFragment", "Using original URL: $url")
                url
            }
        }
    }

    private fun setupInteractiveElements() {
        // Setup rating change listener
        binding.moduleRating.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser && moduleIndex >= 0) {
                moduleViewModel.updateModule(moduleIndex, rating = rating)
                // Auto-save progress when user rates
                updateProgressOnInteraction(10)
            }
        }

        // Setup complete button
        binding.completeButton.setOnClickListener {
            markModuleComplete()
        }

        // Setup notes change listener
        binding.userNotes.addTextChangedListener { text ->
            if (moduleIndex >= 0) {
                moduleViewModel.updateModule(moduleIndex, notes = text.toString())
                // Auto-save progress when user takes notes
                updateProgressOnInteraction(5)
            }
        }

        // Setup feedback change listener
        binding.userFeedback.addTextChangedListener { text ->
            if (moduleIndex >= 0) {
                moduleViewModel.updateModule(moduleIndex, feedback = text.toString())
                // Auto-save progress when user provides feedback
                updateProgressOnInteraction(5)
            }
        }
    }

    private fun updateProgressOnInteraction(progressIncrement: Int) {
        currentModule?.let { module ->
            if (!module.isCompleted && moduleIndex >= 0) {
                val newProgress = minOf(module.progress + progressIncrement, 100)
                moduleViewModel.updateModule(moduleIndex, progress = newProgress)
                
                // Update UI to reflect new progress
                binding.moduleProgressBar.progress = newProgress
                binding.progressText.text = "$newProgress% Complete"
                
                // Show progress update toast
                if (newProgress > module.progress) {
                    ToastUtils.showToast(context, "Progress updated: $newProgress%", "progress_update")
                }
            }
        }
    }

    private fun markModuleComplete() {
        if (moduleIndex >= 0) {
            moduleViewModel.completeModule(moduleIndex)
            updateButtonStates(currentModule?.copy(isCompleted = true))
            ToastUtils.showToast(context, "Module completed! 🎉", "module_complete")
            
            // Show completion celebration
            showCompletionCelebration()
        }
    }

    private fun showCompletionCelebration() {
        // Update progress bar to 100%
        binding.moduleProgressBar.progress = 100
        binding.progressText.text = "100% Complete"
        
        // Show completion badge
        binding.completionBadge.visibility = View.VISIBLE
        binding.completionBadge.setImageResource(R.drawable.ic_launcher_foreground)
        
        // Update button state
        binding.completeButton.text = "Completed ✓"
        binding.completeButton.isEnabled = false
        
        // Show celebration message
        ToastUtils.showToast(context, "Congratulations! Module completed successfully! 🏆", "celebration")
    }

    private fun updateButtonStates(module: LearningModule?) {
        module?.let {
            if (it.isCompleted) {
                binding.completeButton.text = "Completed ✓"
                binding.completeButton.isEnabled = false
                binding.completionBadge.visibility = View.VISIBLE
            } else {
                binding.completeButton.text = "Mark Complete"
                binding.completeButton.isEnabled = true
                binding.completionBadge.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
