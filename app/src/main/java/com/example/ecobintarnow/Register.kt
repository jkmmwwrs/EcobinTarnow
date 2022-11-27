package com.example.ecobintarnow
import android.util.Patterns
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.ecobintarnow.databinding.ActivityRegisterBinding
import com.example.testapp.DatabaseRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore


class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var myRef: DatabaseReference


    private fun emailValidation(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth



        binding.btnregister.setOnClickListener {


            val emailData = binding.inputmail.text.toString()
            val passData = binding.inputpass.text.toString()
            val passData2 = binding.inputpass2.text.toString()
            if (passData == "") {
                Toast.makeText(this, "Wprowadź hasło.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (passData != passData2) {
                    Toast.makeText(this, "Wprowadź dwa identyczne hasła.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (emailValidation(emailData)) {
                        createAccount(emailData, passData)
                    } else {
                        Toast.makeText(this, "Wprowadź poprawny adres email.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "createUserWithEmail:success")
                val user = auth.currentUser
                val userID = auth.currentUser?.uid.toString()

                //Tworzenie urzytkownika w realtimedatabase

                val firebase = FirebaseDatabase.getInstance()
                val FBInput = DatabaseRow(userID, email, 0)

                myRef = firebase.getReference("Users")

                myRef.child(userID).setValue(FBInput)



                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    baseContext, "Błąd danych, możliwe że istnieje już konto powiązane z tymi danymi.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI(null)
            }
        }
        // [END create_user_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {
        val loginData = user?.email
        if (loginData != null) {
            val explicitIntent = Intent(applicationContext, SecondActivity::class.java)
            explicitIntent.putExtra("LOGIN_DATA", loginData)
            Toast.makeText(this, "Zalogowano.", Toast.LENGTH_SHORT)
                .show()
            startActivity(explicitIntent)
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }

}
