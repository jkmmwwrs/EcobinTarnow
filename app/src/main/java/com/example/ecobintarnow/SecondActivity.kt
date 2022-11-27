package com.example.ecobintarnow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ecobintarnow.databinding.ActivityMainBinding
import com.example.ecobintarnow.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}