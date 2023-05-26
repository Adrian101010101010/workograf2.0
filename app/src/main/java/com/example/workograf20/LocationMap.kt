package com.example.workograf20

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location

import android.os.Bundle
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

@Suppress("DEPRECATION")
class LocationMap : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private lateinit var textView: TextView



    companion object {
        private const val TARGET_LATITUDE = 49.777810 // Задайте цільову широту
        private const val TARGET_LONGITUDE = 23.947857 // Задайте цільову довготу

        private const val PERMISSION_REQUEST_CODE = 123
    }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locationmap)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        textView = findViewById(R.id.textView)

        // Check location permission
        if (hasLocationPermission()) {
            requestLocationUpdates()
        } else {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    val targetLocation = LatLng(TARGET_LATITUDE, TARGET_LONGITUDE)

                    val distance = calculateDistance(currentLocation, targetLocation)

                    if (distance <= 1000) {
                        textView.text = "Я в зоні"
                    } else {
                        textView.text = "Я не в зоні"
                    }
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    // Rest of your code...

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied. Unable to track location.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun calculateDistance(startLocation: LatLng, endLocation: LatLng): Float {
        val startPoint = Location("startPoint").apply {
            latitude = startLocation.latitude
            longitude = startLocation.longitude
        }

        val endPoint = Location("endPoint").apply {
            latitude = endLocation.latitude
            longitude = endLocation.longitude
        }

        return startPoint.distanceTo(endPoint)
    }
}