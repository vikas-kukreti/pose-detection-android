package com.vikas.posedetection.data.model

import com.vikas.posedetection.data.model.Content

data class ContentResponse(
    val status: Int,
    val message: String,
    val videos: List<Content>,
    val audios: List<Content>,
    val images: List<Content>
)
