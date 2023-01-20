package com.android.loginapp.presentation.signIn

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.android.loginapp.R
import com.android.loginapp.core.App
import com.android.loginapp.databinding.LoginFragmentBinding
import com.android.loginapp.presentation.profile.FragmentProfile
import com.android.loginapp.presentation.signUp.SignUpFragment
import com.android.loginapp.presentation.signUp.SignUpViewModel
import com.google.android.material.textfield.TextInputLayout

class LoginFragment: Fragment() {


    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel


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
       viewModel= (requireActivity().applicationContext as App).loginViewModel

        viewModel.observeSuccess(viewLifecycleOwner){
            if (it){
                val fragment = FragmentProfile()
                parentFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                    .remove(this)
                    .commit()
                viewModel.stateFalse()
            }
        }

        viewModel.observeState(viewLifecycleOwner){state->
            binding.emailTextInput.isEnabled = state.enableViews
            binding.passwordTextInput.isEnabled = state.enableViews
            binding.signUpButton.isEnabled = state.enableViews
            binding.signInButton.isEnabled = state.enableViews

            fillError(binding.emailTextInput, state.emailErrorMessageRes)
            fillError(binding.passwordTextInput, state.passwordErrorMessageRes)

            binding.progressBar.visibility = if (state.showProgress) View.VISIBLE else View.INVISIBLE

        }

        binding.emailEditText.setText(arguments?.getString(KEY_EMAIL),TextView.BufferType.EDITABLE)



        binding.signInButton.setOnClickListener {
            viewModel.signIn(binding.emailEditText.text.toString(),binding.passwordEditText.text.toString())
        }

        binding.signUpButton.setOnClickListener {
            val fragment = SignUpFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit()
        }

    }


    private fun fillError(input: TextInputLayout, @StringRes errorMessageRes: Int) {

        if (errorMessageRes == SignUpViewModel.NO_ERROR_MESSAGE){
            input.error = null
            input.isErrorEnabled = false
        }else{
            input.error = getString(errorMessageRes)
            input.isErrorEnabled = true
        }
    }




    companion object{

        private const val KEY_EMAIL = "email"

        fun newInstance(email: String): LoginFragment {
            val args = Bundle()
            args.putString(KEY_EMAIL,email)

            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }

}