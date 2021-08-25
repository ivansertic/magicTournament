package com.ivansertic.magictournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton

class MapPopup : DialogFragment() {

    private lateinit var mMap: GoogleMap
    private var mapReady = false
    var lat : Double = 0.0
    var long: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainView: View = inflater.inflate(R.layout.popup_map, container,false)

        val mapFragment = fragmentManager?.findFragmentById(R.id.mapViewPopup) as SupportMapFragment

        mapFragment.getMapAsync{
            googleMap -> mMap = googleMap
            mapReady = true
            showLocation()
        }
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeButton = view.findViewById<MaterialButton>(R.id.popupClose)

        closeButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(fragmentManager?.findFragmentById(R.id.mapViewPopup) as SupportMapFragment)?.commit()
            this.dismiss()
        }
    }

    private fun showLocation(){
        if(mapReady){
           val mapMarker = LatLng(lat,long)
            mMap.addMarker(MarkerOptions().position(mapMarker))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapMarker,17.0f))
        }
    }
}
