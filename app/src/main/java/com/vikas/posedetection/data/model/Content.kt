package com.vikas.posedetection.data.model

data class Content(
    val id: Int,
    val user_id: Int,
    val path: String,
    val uploaded: String,
    val city: String,
    val branch: String,
    val remarks: String,
    val size: Long,
    val duration: Long
)
