package com.vikas.posedetection.app

import android.app.Application
import com.vikas.posedetection.data.AppRepository

class PoseDetection: Application() {
    override fun onCreate() {
        super.onCreate()
        AppRepository.initialize(applicationContext)
    }
}