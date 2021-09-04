package com.vikas.posedetection.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.vikas.posedetection.R
import com.vikas.posedetection.ui.auth.LoginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        if (findNavController(R.id.nav_host_fragment).currentDestination?.id != R.id.mainFragment) {
            findNavController(R.id.nav_host_fragment).popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}