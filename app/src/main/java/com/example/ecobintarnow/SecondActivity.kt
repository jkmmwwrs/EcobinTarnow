package com.example.ecobintarnow
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.ecobintarnow.MainActivity
import com.example.ecobintarnow.QR
import com.example.ecobintarnow.databinding.ActivitySecondBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker

class SecondActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    val mDatabase = FirebaseDatabase.getInstance().getReference("Users");

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val userID = auth.currentUser?.uid
        var point:String = "0"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //pobieranie liczby punktów z bazy

        mDatabase.child(auth.currentUser?.uid.toString()).get().addOnSuccessListener {
            point = it.child("points").value.toString()
            binding.points.text = point
        }

        binding.buttonmap.setOnClickListener {
//            val eIntentToMap = Intent(applicationContext, MapsActivity::class.java)
//            startActivity(eIntentToMap)
        }

        if(intent.hasExtra("LOGIN_DATA")){
            binding.textbox2.text = intent.getStringExtra("LOGIN_DATA")
        }

        binding.button.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val explicitIntent = Intent(applicationContext, MainActivity::class.java)
            Toast.makeText(this, "Wylogowano.", Toast.LENGTH_SHORT)
                .show()
            startActivity(explicitIntent)
        }

        binding.button2.setOnClickListener{
            val eIntentToQR = Intent(applicationContext, QR::class.java)
            eIntentToQR.putExtra("PKT_DATA", point)
            startActivity(eIntentToQR)

        }
        binding.buttonforum.setOnClickListener{
            val eIntentToForum = Intent(applicationContext, ForumActivity::class.java)
            startActivity(eIntentToForum)

        }

        /*if(intent.hasExtra("PASS_DATA")){
            binding.textbox3.text = intent.getStringExtra("PASS_DATA")
        }*/
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }
    private fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this){ location->
            if(location !=null){
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
            }
            else
            {
                val start = LatLng(50.02123595956695, 20.980167666135213)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start,12f))
            }

        }
    }
    private fun placeMarkerOnMap(currentLatLong: LatLng){
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("Tu jesteś!")
        mMap.addMarker(markerOptions)

        val zsme = LatLng(50.015327417015165, 20.975568497898742)
        val hackathon = LatLng(50.02369639278428, 20.98445644395501)
        mMap.addMarker(MarkerOptions().position(zsme).title("ZSME"))
        mMap.addMarker(MarkerOptions().position(hackathon).title("Hackathon"))
    }
    override fun onMarkerClick(p0: Marker)= false

}