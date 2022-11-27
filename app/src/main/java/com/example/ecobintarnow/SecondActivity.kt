package com.example.ecobintarnow
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.example.ecobintarnow.MainActivity
import com.example.ecobintarnow.QR
import com.example.ecobintarnow.databinding.ActivitySecondBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference
    val mDatabase = FirebaseDatabase.getInstance().getReference("Users");


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val userID = auth.currentUser?.uid
        var point:String = "0"

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
}