package com.ivansertic.magictournament.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.card.MaterialCardView


class CreateTournament : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mapMarker: Marker
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
                mapMarker = mMap.addMarker(MarkerOptions().position(location))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,17.0f))

                findViewById<MaterialCardView>(R.id.cvLocation).visibility = View.VISIBLE
            }
        }


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setOnClickListeners()
    }


    private fun setOnClickListeners() {

        val cancelButton: MaterialButton = findViewById(R.id.btnCancel)
        val submitButton: MaterialButton = findViewById(R.id.btnSubmit)
        val cardViewCancel: MaterialButton = findViewById(R.id.cvCancel)
        val cardViewAccept: MaterialButton = findViewById(R.id.cvAccept)
        val country: TextInputLayout = findViewById(R.id.etCountry)
        val city: TextInputLayout = findViewById(R.id.etCity)
        val address: TextInputLayout = findViewById(R.id.etAddress)
        val cardView: MaterialCardView = findViewById(R.id.cvLocation)
        val geocoder = Geocoder(this)


        cancelButton.setOnClickListener {
            this.finish()
        }

        submitButton.setOnClickListener {
            reloadMapData(country, city, address, cardView, geocoder)
        }

        cardViewCancel.setOnClickListener{
            cancelButton.visibility = View.VISIBLE
            submitButton.visibility = View.VISIBLE
            country.visibility = View.VISIBLE
            city.visibility = View.VISIBLE
            address.visibility = View.VISIBLE
            cardView.visibility = View.GONE

            val userAddress = geocoder.getFromLocation(myLatitude,myLongitude,1)
            city.editText?.setText(userAddress[0].locality.toString())
            country.editText?.setText(userAddress[0].countryName.toString())
            address.editText?.setText(userAddress[0].getAddressLine(0).toString())

        }

        cardViewAccept.setOnClickListener {
            startActivity(Intent(this,TournamentAdditional::class.java))
        }
    }

    private fun reloadMapData(country: TextInputLayout, city:TextInputLayout, address: TextInputLayout, cardView: MaterialCardView, geocoder: Geocoder){

        val countryText: String = country.editText?.text.toString()
        val cityText: String = city.editText?.text.toString()
        val addressText: String = address.editText?.text.toString()

        if(countryText.isBlank() || cityText.isBlank() || addressText.isBlank()){
            return
        }

        val newAddressString = "$countryText $cityText $addressText"

        val newAddress = geocoder.getFromLocationName(newAddressString,1)

        if(!newAddress[0].latitude.isNaN() && !newAddress[0].longitude.isNaN()){
            if(myLatitude != newAddress[0].latitude || myLongitude != newAddress[0].longitude){
                myLatitude = newAddress[0].latitude
                myLongitude = newAddress[0].longitude

                val location = LatLng(myLatitude,myLongitude)
                mapMarker.remove()
                mapMarker = mMap.addMarker(MarkerOptions().position(location))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15.0f))
            }
        }

        cardView.visibility = View.VISIBLE

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
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