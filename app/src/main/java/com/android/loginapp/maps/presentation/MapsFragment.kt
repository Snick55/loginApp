package com.android.loginapp.maps.presentation

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.android.loginapp.R
import com.android.loginapp.core.App
import com.android.loginapp.databinding.FragmentProfileBinding
import com.android.loginapp.login.presentation.signIn.LoginFragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

class MapsFragment : Fragment() {

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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireContext().applicationContext as App).mapsViewModel
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val traffic = MapKitFactory.getInstance().createTrafficLayer(binding.mapView.mapWindow)
        viewModel.getUsername()
        viewModel.getLocation()

        viewModel.currentName.observe(viewLifecycleOwner) {
            binding.toolbar.title = it
        }
        viewModel.currentLocation.observe(viewLifecycleOwner) {
            binding.mapView.map.move(
                CameraPosition(Point(it.first, it.second), 15.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1.5F), null
            )
        }

        var isTrafficActive = false
        binding.findMeButton.setOnClickListener { viewModel.getLocation() }
        binding.trafficButton.setOnClickListener {
            it.setBackgroundResource(R.drawable.ic_traffic_green)
            if (!isTrafficActive) {
                traffic.isTrafficVisible = true
                isTrafficActive = !isTrafficActive
            } else {
                it.setBackgroundResource(R.drawable.ic_traffic)
                traffic.isTrafficVisible = false
                isTrafficActive = !isTrafficActive
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.signOut -> {
                viewModel.signOut()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LoginFragment())
                    .remove(this)
                    .commit()
            }
            R.id.change_name -> {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                DialogManager.changePass(dialogBuilder) {
                    viewModel.changeName(it)
                }
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
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }
}