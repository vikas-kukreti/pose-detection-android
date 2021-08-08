package com.vikas.posedetection.data

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.pose.Pose

class AppRepository {

    val poseBitmap = MutableLiveData<Bitmap>()
    val pose = MutableLiveData<Pose>()
    fun setPoseData(pose: Pose) {
        this.pose.value = pose
    }
    fun setPoseBitmap(poseBitmap: Bitmap) {
        this.poseBitmap.value = poseBitmap
    }
    companion object {
        private lateinit var appRepository: AppRepository
        fun initialize() {
            appRepository = AppRepository()
        }
        fun get(): AppRepository {
            if(!this::appRepository.isInitialized) initialize()
            return appRepository
        }
    }
}