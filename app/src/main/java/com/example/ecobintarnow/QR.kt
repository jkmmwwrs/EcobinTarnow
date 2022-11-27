package com.example.ecobintarnow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.inflate
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.example.ecobintarnow.R.*
import com.example.ecobintarnow.databinding.ActivityQrBinding
import com.example.testapp.DatabaseRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class QR : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: ActivityQrBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference
    val mDatabase = FirebaseDatabase.getInstance().getReference("Users");


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnBack = binding.btnBack
//        val textField = binding.textView

        btnBack.setOnClickListener(View.OnClickListener { startActivity(Intent(this, SecondActivity::class.java)) })

        val scannerView = binding.scannerView

        codeScanner = CodeScanner(this, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        auth = Firebase.auth


        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val userID = auth.currentUser?.uid.toString()
                val email = auth.currentUser?.email.toString()

//                if(intent.hasExtra("PKT_DATA")){
//                    val points = intent.getStringExtra("PKT_DATA")?.toInt()?.plus(5)
//                }
                val points = intent.getStringExtra("PKT_DATA")?.toInt()?.plus(5)
                val firebase = FirebaseDatabase.getInstance()

                val FBInput = DatabaseRow(userID, email, points)

                myRef = firebase.getReference("ArrayData")
                myRef.child(userID).setValue(FBInput)



                val explicitIntent = Intent(applicationContext, SecondActivity::class.java)
                explicitIntent.putExtra("QR_DATA", it.text)
                startActivity(explicitIntent)
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(
                    this, "Błąd wczytywania kamery: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}


