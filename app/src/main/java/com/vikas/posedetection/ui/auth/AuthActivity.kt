package com.vikas.posedetection.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vikas.posedetection.R
import com.vikas.posedetection.databinding.ActivityAuthBinding
import com.vikas.posedetection.databinding.MainFragmentBinding
import com.vikas.posedetection.ui.main.MainViewModel

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.authContainer, LoginFragment.newInstance())
                .commitNow()
        }

    }
}