package com.fpoly.shoes_app.framework.presentation.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentAddressDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.PlacesClient

class AddressDetailsFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentAddressDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var token: AutocompleteSessionToken

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressDetailsBinding.inflate(inflater, container, false)
        token = AutocompleteSessionToken.newInstance()

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        context?.let {
            Places.initialize(it, getString(R.string.gg_map))
            placesClient = Places.createClient(it)
        }
        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val location = LatLng( 21.039430738632642, 105.83454867959188) // Lang Bac
        val customMarker = BitmapDescriptorFactory.fromResource(R.drawable.baseline_add_location_alt_24)
        mMap.addMarker(MarkerOptions().position(location).title("Vị trí đã chọn").icon(context?.let {
            BitmapDescriptor(
                it, R.drawable.baseline_add_location_alt_24
            )
        }))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f))
    }

    private  fun BitmapDescriptor(context: Context,vector:Int):BitmapDescriptor{
        var vectorDraw= ContextCompat.getDrawable(context,vector)
        vectorDraw?.let { vectorDraw.setBounds(0,0, it.intrinsicWidth,vectorDraw.intrinsicHeight)}
        var bitmap = vectorDraw?.let { Bitmap.createBitmap(it.intrinsicWidth,vectorDraw.intrinsicHeight,Bitmap.Config.ARGB_8888) }
        var canvar = bitmap?.let { Canvas(it) }
        canvar?.let {
            if (vectorDraw != null) {
                vectorDraw.draw(
                    it
                )
            }
        }
        return BitmapDescriptorFactory.fromBitmap(bitmap!!)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
