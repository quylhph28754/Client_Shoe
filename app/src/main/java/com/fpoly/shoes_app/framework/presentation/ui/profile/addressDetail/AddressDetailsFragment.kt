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
import com.fpoly.shoes_app.framework.domain.model.profile.address.Addresse
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
    private var bundle: Addresse? = null
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

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        bundle?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            updateMapAndAddress(latLng, it.detailAddress)
        }
    }
    private fun updateMapAndAddress(latLng: LatLng, address: String) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(latLng).title(address))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))
        Log.d("AddressDetailsFragment", "Updated map with address: $address")
        binding.nameAddressEditText.hint = bundle?.nameAddress ?: ""
        binding.addressDetailEditText.hint = address
    }
}
