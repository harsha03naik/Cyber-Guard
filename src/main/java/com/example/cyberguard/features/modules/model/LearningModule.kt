package com.example.cyberguard.features.modules.model

import androidx.annotation.DrawableRes
import java.io.Serializable

data class LearningModule(
    val title: String,
    val description: String,
    @DrawableRes val imageResId: Int,
    val videoUrl: String? = null,
    val codeSample: String? = null,
    var userNotes: String = "",
    var userRating: Float = 0f,
    var userFeedback: String = "",
    var isCompleted: Boolean = false,
    var isUnlocked: Boolean = false,
    var progress: Int = 0,
    @DrawableRes var completionBadgeResId: Int? = null
) : Serializable
