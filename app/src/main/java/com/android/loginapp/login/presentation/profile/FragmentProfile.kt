package com.android.loginapp.login.presentation.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.loginapp.R
import com.android.loginapp.core.App
import com.android.loginapp.databinding.FragmentProfileBinding
import com.android.loginapp.login.presentation.signIn.LoginFragment
//
//class FragmentProfile : Fragment() {
//
//    private lateinit var binding: FragmentProfileBinding
//    private lateinit var viewModel: ProfileViewModel
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentProfileBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewModel = (requireActivity().applicationContext as App).profileViewModel
//        viewModel.currentName.observe(viewLifecycleOwner) {
//            binding.email.text = it
//        }
//        binding.signOutButton.setOnClickListener {
//            viewModel.signOut()
//            val fragment = LoginFragment()
//            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
//                .commit()
//
//        }
//
//        binding.changeNameButton.setOnClickListener {
//            val dialogBuilder = AlertDialog.Builder(requireContext())
//            DialogManager.changePass(dialogBuilder){
//                viewModel.changeName(it)
//            }
//        }
//    }
//
//
//
//    override fun onStart() {
//        super.onStart()
//        viewModel.currentUser()
//    }
//
//}