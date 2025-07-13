package com.example.cyberguard.features.modules

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberguard.R
import com.example.cyberguard.features.modules.model.LearningModule

class ModuleAdapter(
    private val onModuleClicked: (LearningModule) -> Unit
) : ListAdapter<LearningModule, ModuleAdapter.ModuleViewHolder>(ModuleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_module_card, parent, false)
        return ModuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val module = getItem(position)
        holder.bind(module, onModuleClicked)
    }

    class ModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.module_title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.module_description)
        private val imageView: ImageView = itemView.findViewById(R.id.module_image)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.module_progress)
        private val lockIcon: ImageView = itemView.findViewById(R.id.module_lock_icon)
        private val progressText: TextView = itemView.findViewById(R.id.progress_text)
        private val completionBadge: ImageView = itemView.findViewById(R.id.completion_badge)
        private val moduleRating: RatingBar = itemView.findViewById(R.id.module_rating)

        fun bind(
            module: LearningModule,
            onModuleClicked: (LearningModule) -> Unit
        ) {
            titleTextView.text = module.title
            descriptionTextView.text = module.description
            imageView.setImageResource(module.imageResId)
            progressBar.progress = module.progress
            
            // Update progress text
            val progressTextValue = when {
                module.isCompleted -> "Completed ✓"
                module.progress > 0 -> "${module.progress}% Complete"
                else -> "Not Started"
            }
            progressText.text = progressTextValue
            
            // Show/hide completion badge
            if (module.isCompleted) {
                completionBadge.visibility = View.VISIBLE
                completionBadge.setImageResource(
                    module.completionBadgeResId ?: R.drawable.ic_launcher_foreground
                )
            } else {
                completionBadge.visibility = View.GONE
            }
            
            // Show/hide rating
            if (module.userRating > 0) {
                moduleRating.visibility = View.VISIBLE
                moduleRating.rating = module.userRating
            } else {
                moduleRating.visibility = View.GONE
            }
            
            // Enhanced unlock/lock display
            if (module.isUnlocked) {
                lockIcon.visibility = View.GONE
                itemView.isEnabled = true
                itemView.alpha = 1.0f
                itemView.setOnClickListener { 
                    android.util.Log.d("ModuleAdapter", "Module clicked: ${module.title}")
                    onModuleClicked(module) 
                }
                
                // Add ripple effect for unlocked modules
                itemView.isClickable = true
                itemView.isFocusable = true
                android.util.Log.d("ModuleAdapter", "Module ${module.title} is unlocked and clickable")
            } else {
                lockIcon.visibility = View.VISIBLE
                itemView.isEnabled = false
                itemView.alpha = 0.6f
                itemView.setOnClickListener(null)
                itemView.isClickable = false
                itemView.isFocusable = false
                android.util.Log.d("ModuleAdapter", "Module ${module.title} is locked")
                
                // Show unlock requirement
                val previousModule = if (adapterPosition > 0) {
                    "Complete previous module"
                } else {
                    "Complete previous module"
                }
                descriptionTextView.text = "$previousModule to unlock"
            }
        }
    }

    private class ModuleDiffCallback : DiffUtil.ItemCallback<LearningModule>() {
        override fun areItemsTheSame(oldItem: LearningModule, newItem: LearningModule): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: LearningModule, newItem: LearningModule): Boolean {
            return oldItem == newItem
        }
    }
}
