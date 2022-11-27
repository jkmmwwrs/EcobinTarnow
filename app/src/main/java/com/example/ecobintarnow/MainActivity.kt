package com.example.ecobintarnow
import android.util.Patterns
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ecobintarnow.databinding.ActivityMainBinding
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    private fun emailValidation(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth


        binding.btnregister.setOnClickListener {
            val explicitIntent = Intent(applicationContext, Register::class.java)
            startActivity(explicitIntent)
        }
        binding.btnlog.setOnClickListener {


            val emailData = binding.loginmail.text.toString()
            val passData = binding.loginpass.text.toString()
            if(passData=="") {
                Toast.makeText(this, "Wprowadź hasło.", Toast.LENGTH_SHORT)
                    .show()
            }
            else
            {
                if (emailValidation(emailData)) {
                    signIn(emailData, passData)

                } else {
                    Toast.makeText(this, "Wprowadź poprawny adres email.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload(currentUser)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {

                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Nie udało się zalogować.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }

    }


    private fun updateUI(user: FirebaseUser?) {
        val loginData = user?.email
        if(loginData!=null) {
            val explicitIntent = Intent(applicationContext, SecondActivity::class.java)
            explicitIntent.putExtra("LOGIN_DATA", loginData)
            Toast.makeText(this, "Zalogowano.", Toast.LENGTH_SHORT)
                .show()
            startActivity(explicitIntent)
        }
    }

    public fun reload(user: FirebaseUser?) {
        Log.w(TAG, "loggedOnRun")
        val loginData = user?.email
        if(loginData!=null) {
            val explicitIntent = Intent(applicationContext, SecondActivity::class.java)
            explicitIntent.putExtra("LOGIN_DATA", loginData)
            startActivity(explicitIntent)
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }


}

