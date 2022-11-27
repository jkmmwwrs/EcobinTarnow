package com.example.ecobintarnow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.example.ecobintarnow.R.*
import com.example.ecobintarnow.databinding.ActivityQrBinding
import com.example.ecobintarnow.DatabaseRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
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
        val btnGen = binding.btnGen
//        val textField = binding.textView

        btnBack.setOnClickListener{
            startActivity(Intent(applicationContext, SecondActivity::class.java))
        }

        btnGen.setOnClickListener{
            Log.e("CHANGE", "YES")
            startActivity(Intent(applicationContext, HiddenQRGenerator::class.java))
        }

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
                val kodZ = it.text.toString()

                val points = intent.getStringExtra("PKT_DATA")?.toInt()?.plus(kodZ[7].code)


                val firebase = FirebaseDatabase.getInstance()

                val FBInput = DatabaseRow(userID, email, points)

                myRef = firebase.getReference("Users")
                myRef.child(userID).setValue(FBInput)


                val xDatabase = FirebaseDatabase.getInstance().getReference("QRCodes");

                xDatabase.get().addOnSuccessListener {
                    val test = it.child(kodZ).value.toString()
                    Toast.makeText(
                        applicationContext, test,
                        Toast.LENGTH_LONG
                    ).show()
                }


//                val rootRef = FirebaseDatabase.getInstance().getReference("QRCode")
//                rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        if (snapshot.hasChild("88881623")) {
//                            Toast.makeText(
//                                applicationContext, "Success",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                        else{
//                            Toast.makeText(
//                                applicationContext, "Błąd w sukcesie",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//
//                    }

//                val testdb = FirebaseDatabase.getInstance().getReference("QRCode")
//                val test = testdb.child("QRCode")

//                    override fun onCancelled(error: DatabaseError) {
//                        Toast.makeText(
//                            applicationContext, "Błąd",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                })





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


        if(auth.currentUser?.uid == "X6C6xHu2HGVXV6iiGQUNqCXG9Q32"){
            binding.btnGen.visibility = View.VISIBLE
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


