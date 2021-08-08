package com.vikas.posedetection.ui.result

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.pose.Pose
import com.vikas.posedetection.data.AppRepository

class ResultViewModel : ViewModel() {
    val repository = AppRepository.get()
    val pose: LiveData<Pose> get() =  repository.pose
    val poseBitmap: LiveData<Bitmap> get() =  repository.poseBitmap

}