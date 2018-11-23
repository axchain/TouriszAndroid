package com.tourisz.user.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tourisz.util.font.TypefaceUtil

open class BaseActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TypefaceUtil.overrideFonts(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLoaction()

    }

    fun getLastLocation(): Location?{
        return lastLocation
    }

    private val LOC_REQUEST_CODE = 80

    fun checkLoaction() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        LOC_REQUEST_CODE)
            } else {
                fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            lastLocation = location
                        }
            }
        } else {
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        lastLocation = location
                    }
        }

    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      if (requestCode == LOC_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            lastLocation = location
                        }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

}
