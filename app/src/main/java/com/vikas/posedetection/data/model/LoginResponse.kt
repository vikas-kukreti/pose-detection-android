package com.vikas.posedetection.data.model

data class LoginResponse(val status: Int, val message: String, val token: String?, val user: User?)
