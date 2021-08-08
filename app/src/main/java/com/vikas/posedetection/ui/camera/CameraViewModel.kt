package com.vikas.posedetection.ui.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.pose.Pose
import com.vikas.posedetection.data.AppRepository

class CameraViewModel : ViewModel() {
    fun setPoseData(pose: Pose) {
        AppRepository.get().setPoseData(pose)
    }

    fun setPoseBitmap(poseBitmap: Bitmap) {
        AppRepository.get().setPoseBitmap(poseBitmap)
    }
}