package com.fpoly.shoes_app.framework.presentation.ui.profile.addressDetail

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentAddressDetailsBinding
import com.fpoly.shoes_app.framework.domain.model.profile.address.AddressDetail
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AddressDetailsFragment : BaseFragment<FragmentAddressDetailsBinding, AddressDetailsViewModel>(
    FragmentAddressDetailsBinding::inflate, AddressDetailsViewModel::class.java
), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var bundle: AddressDetail? = null
    override fun setupViews() {
        bundle = arguments?.getParcelable("address")
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun setupPreViews() {

    }
    override fun bindViewModel() {

    }

    override fun setOnClick() {
//        binding.searchMap.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                query?.let {
//                    searchLocationByName(it)
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        bundle?.let {
            val latLng = LatLng(it.lat, it.long)
            updateMapAndAddress(latLng, it.address)
        }
    }

    private fun searchLocationByName(locationName: String) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocationName(locationName, 1)!!
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val latLng = LatLng(address.latitude, address.longitude)
                updateMapAndAddress(latLng, address.getAddressLine(0))
            } else {
                binding.addressDetailEditText.hint = "Location not found"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.addressDetailEditText.hint = "Error occurred"
        }
    }

    private fun updateMapAndAddress(latLng: LatLng, address: String) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(latLng).title(address))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f))
        Log.d("AddressDetailsFragment", "Updated map with address: $address")
        binding.nameAddressEditText.hint = bundle?.name ?: ""
        binding.addressDetailEditText.hint = address
    }
}
