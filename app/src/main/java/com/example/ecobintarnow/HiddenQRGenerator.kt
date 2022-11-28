package com.example.ecobintarnow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ecobintarnow.R
import com.example.ecobintarnow.databinding.ActivityHiddenQrgeneratorBinding
import com.example.ecobintarnow.databinding.ActivityMainBinding
import com.example.ecobintarnow.databinding.ActivityQrBinding
import com.example.ecobintarnow.DatabaseRow
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.lang.System.currentTimeMillis

class HiddenQRGenerator: AppCompatActivity()
{
    private lateinit var myRef: DatabaseReference
    private lateinit var binding: ActivityHiddenQrgeneratorBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityHiddenQrgeneratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnGenerateQRCode = binding.btnGenerateQRCode



        btnGenerateQRCode.setOnClickListener{
            val text1 = (0..9).random().toString() + (0..9).random().toString() + (0..9).random().toString() + (0..9).random().toString() + (0..9).random().toString() + (0..9).random().toString() + (0..9).random().toString() + (1..4).random().toString()

            btnGenerateQRCode.text = text1



            val encoder = BarcodeEncoder()
            val bitmap = encoder.encodeBitmap(text1, BarcodeFormat.QR_CODE, 700, 700)
            val myImageView: ImageView = findViewById(R.id.myImageView)

            myImageView.setImageBitmap(bitmap)


            val firebase = FirebaseDatabase.getInstance()
            val FBInput = text1

            myRef = firebase.getReference("QRCodes")

            myRef.child(FBInput).setValue(FBInput)
        }
    }
}