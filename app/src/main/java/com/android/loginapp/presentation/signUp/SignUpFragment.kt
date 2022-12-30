package com.android.loginapp.presentation.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.loginapp.databinding.SignUpFragmentBinding

class SignUpFragment: Fragment() {

    private lateinit var binding: SignUpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignUpFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createAccountButton.setOnClickListener {
            with(binding){
                // TODO: make validate on password
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                val name = usernameEditText.text.toString()
                viewModel.signUp(email,password)
            }



        }



    }

}