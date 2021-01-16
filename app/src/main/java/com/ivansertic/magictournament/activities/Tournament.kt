package com.ivansertic.magictournament.activities

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ivansertic.magictournament.R
import java.io.IOException

class Tournament : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        try {
            val newLocation: LatLng = this.getLatLong("Ljudevita Posavskog 18, Osijek")

            mMap.addMarker(MarkerOptions().position(newLocation).title("Marker in Sydney"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation,17.0f))
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

    private fun getLatLong(address: String): LatLng {
        val geocoder = Geocoder(this)
        val newAddress = geocoder.getFromLocationName(address, 1)
        val latitude = newAddress[0].latitude
        val longitude = newAddress[0].longitude

        return LatLng(latitude, longitude)
    }
}