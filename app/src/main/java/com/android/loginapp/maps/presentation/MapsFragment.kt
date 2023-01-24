package com.android.loginapp.maps.presentation

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.loginapp.R
import com.android.loginapp.core.App
import com.android.loginapp.databinding.ActivityMainBinding
import com.android.loginapp.databinding.FragmentProfileBinding
import com.android.loginapp.login.presentation.signIn.LoginFragment
import com.android.loginapp.maps.MainViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

class MapsFragment: Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: MapsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        MapKitFactory.initialize(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireContext().applicationContext as App).mapsViewModel
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        viewModel.getUsername()
        viewModel.currentName.observe(viewLifecycleOwner){
            binding.toolbar.title = it
        }


        binding.mapView.map.move(CameraPosition(Point(55.751574, 37.573856),11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),null)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.signOut -> {
                viewModel.signOut()
                parentFragmentManager.beginTransaction().replace(R.id.fragment_container,LoginFragment())
                    .remove(this)
                    .commit()
            }
        }
        return true
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart();
      binding.mapView.onStart();
    }



}