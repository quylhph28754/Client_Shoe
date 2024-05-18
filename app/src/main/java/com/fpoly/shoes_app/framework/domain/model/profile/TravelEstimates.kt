package com.fpoly.shoes_app.framework.domain.model.profile

import android.content.ContentValues.TAG
import android.util.Log
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode

typealias TravelEstimationsCallback = (TravelEstimations?) -> Unit

data class TravelEstimations(val distance: String?, val duration: String?)

fun getTravelEstimations(
     apiKey: String,
     source: LatLng,
     destination: LatLng,
     callback: TravelEstimationsCallback
) {
     val context = GeoApiContext.Builder().apiKey(apiKey).build()
     DirectionsApi.newRequest(context)
          .mode(TravelMode.DRIVING)
          .origin(LatLng(source.lat, source.lng))
          .destination(LatLng(destination.lat, destination.lng))
          .await().run {
               if (routes.isNotEmpty()) {
                    val route = routes[0]
                    val leg = route.legs[0]
                    val distance = leg.distance.humanReadable
                    val duration = leg.duration.humanReadable
                    val estimations = TravelEstimations(distance, duration)
                    callback(estimations)
               } else {
                    callback(null)
                    Log.e(TAG, "No routes found")
               }
          }
}