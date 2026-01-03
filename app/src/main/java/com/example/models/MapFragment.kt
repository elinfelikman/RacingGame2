package com.example.models

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.racinggame1.R

class MapFragment : Fragment() {

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_map, container, false)

        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment?.getMapAsync { map ->
            googleMap = map
        }

        return v
    }


    fun zoomToLocation(lat: Double, lng: Double) {
        val location = LatLng(lat, lng)
        googleMap?.clear()
        googleMap?.addMarker(MarkerOptions().position(location).title("Score Location"))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}