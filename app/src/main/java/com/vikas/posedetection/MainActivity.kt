package com.vikas.posedetection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vikas.posedetection.ui.camera.CameraFragment
import com.vikas.posedetection.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CameraFragment.newInstance())
                .commitNow()
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.size > 1) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }
}