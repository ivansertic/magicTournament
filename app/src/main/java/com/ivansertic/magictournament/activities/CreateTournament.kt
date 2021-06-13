package com.ivansertic.magictournament.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.ivansertic.magictournament.R
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.security.auth.callback.Callback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class CreateTournament : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var myLatitude: Double = 0.0
    private var myLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = LocationServices.getFusedLocationProviderClient(this)
        setContentView(R.layout.activity_create_tournament)

        locationRequest = LocationRequest.create()
        locationRequest.interval =5000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                myLongitude = locationResult.lastLocation.longitude
                myLatitude = locationResult.lastLocation.latitude
                locationManager.removeLocationUpdates(this)

                val location = LatLng(myLatitude,myLongitude)
                mMap.addMarker(MarkerOptions().position(location))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,17.0f))
            }
        }
        getMyLocation()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setOnClickListeners()
    }

    private fun getMyLocation() {

    }

    private fun setOnClickListeners() {

        val cancelButton: MaterialButton = findViewById(R.id.btnCancel)
        val submitButton: MaterialButton = findViewById(R.id.btnSubmit)

        cancelButton.setOnClickListener {
            this.finish()
        }

        submitButton.setOnClickListener {
            val country: TextInputLayout = findViewById(R.id.etCountry)
            val city: TextInputLayout = findViewById(R.id.etCity)
            val address: TextInputLayout = findViewById(R.id.etAddress)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            return
        } else {

            locationManager.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
        }

    }

}