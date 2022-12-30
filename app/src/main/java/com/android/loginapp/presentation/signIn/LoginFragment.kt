package com.android.loginapp.presentation.signIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.loginapp.R
import com.android.loginapp.databinding.LoginFragmentBinding
import com.android.loginapp.databinding.SignUpFragmentBinding
import com.android.loginapp.presentation.signUp.SignUpFragment

class LoginFragment: Fragment() {


    private lateinit var binding: LoginFragmentBinding
    private val viewModel = LoginViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {
            viewModel.signIn(binding.emailEditText.text.toString(),binding.passwordEditText.text.toString())
        }

        binding.signUpButton.setOnClickListener {
            val fragment = SignUpFragment()
            parentFragmentManager.beginTransaction()
                .add(R.id.fragment_container,fragment)
                .commit()
        }

    }

}