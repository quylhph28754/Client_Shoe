package com.fpoly.shoes_app.framework.presentation.ui.profile.addressDetail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentAddressDetailsBinding
import com.fpoly.shoes_app.framework.domain.model.profile.AddressDetail
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class AddressDetailsFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentAddressDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap
    private lateinit var name: String
    private lateinit var address: String
    private var lat = 0.0
    private var long = 0.0
    private lateinit var placesClient: PlacesClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressDetailsBinding.inflate(inflater, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        context?.let {
            Places.initialize(it, getString(R.string.key_map))
            placesClient = Places.createClient(it)
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<AddressDetail>("address")?.let { myObject ->
            name = myObject.name
            address = myObject.address
            lat = myObject.lat
            long = myObject.long
        }
        binding.nameAddressEditText.setText(name)
        binding.addressDetailEditText.setText(address)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val destination = LatLng(lat, long)
        val cameraPosition = CameraPosition.Builder().target(destination).zoom(18f).build()
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        val markerOptions = MarkerOptions().position(destination).title("Location")
        googleMap.addMarker(markerOptions)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
