package com.android.loginapp.presentation.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.android.loginapp.R
import com.android.loginapp.core.App
import com.android.loginapp.databinding.SignUpFragmentBinding
import com.android.loginapp.presentation.signIn.LoginFragment
import com.android.loginapp.presentation.signUp.SignUpViewModel.Companion.NO_ERROR_MESSAGE
import com.google.android.material.textfield.TextInputLayout

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
        val viewModel = (requireActivity().applicationContext as App).viewModel

        viewModel.successLiveData.observe(viewLifecycleOwner){
            if (it) {
                Toast.makeText(requireContext(), R.string.user_created, Toast.LENGTH_SHORT).show()

                val fragment = LoginFragment.newInstance(binding.emailEditText.text.toString())
                parentFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                    .commit()
                viewModel.stateFalse()
            }
        }

        viewModel.state.observe(viewLifecycleOwner){state->
            binding.createAccountButton.isEnabled = state.enableViews
            binding.emailTextInput.isEnabled = state.enableViews
            binding.usernameTextInput.isEnabled = state.enableViews
            binding.passwordTextInput.isEnabled = state.enableViews
            binding.repeatPasswordTextInput.isEnabled = state.enableViews

            fillError(binding.emailTextInput, state.emailErrorMessageRes)
            fillError(binding.usernameTextInput, state.usernameErrorMessageRes)
            fillError(binding.passwordTextInput, state.passwordErrorMessageRes)
            fillError(binding.repeatPasswordTextInput, state.repeatPasswordErrorMessageRes)

            binding.progressBar.visibility = if (state.showProgress) View.VISIBLE else View.INVISIBLE

        }


        binding.createAccountButton.setOnClickListener {
            with(binding){
                // TODO: make validate on password
                val email = binding.emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                val name = usernameEditText.text.toString()
                val repeatPass = repeatPasswordEditText.text.toString()
                viewModel.signUp(name,email,password,repeatPass)




            }
        }


    }

    private fun fillError(input: TextInputLayout, @StringRes errorMessageRes: Int) {

        if (errorMessageRes == NO_ERROR_MESSAGE){
            input.error = null
            input.isErrorEnabled = false
        }else{
            input.error = getString(errorMessageRes)
            input.isErrorEnabled = true
        }
    }


}