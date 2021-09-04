package com.vikas.posedetection.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vikas.posedetection.SplashActivity
import com.vikas.posedetection.R
import com.vikas.posedetection.data.AppRepository
import com.vikas.posedetection.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        init()
    }

    private fun init() {
        binding.login.setOnClickListener {
            hideKeyboard()
            binding.emailInput.clearFocus()
            binding.passwordInput.clearFocus()
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Snackbar.make(binding.root, getString(R.string.invalid_email_address), Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(password.length < 5) {
                Snackbar.make(binding.root, getString(R.string.password_atleast_5_chars), Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.login.visibility = View.GONE
            binding.loading.visibility = View.VISIBLE

            AppRepository.get().login(email, password){
                if(it.status == 1) {
                    AppRepository.get().saveLoginInfo(it.token, it.user)
                    val intent = Intent(requireContext(), SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Snackbar.make(binding.root, it.message, Snackbar.LENGTH_LONG).show()
                    binding.login.visibility = View.VISIBLE
                    binding.loading.visibility = View.GONE
                }

            }
        }
    }


    private fun hideKeyboard() {
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        activity?.currentFocus?.let {
            inputManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}