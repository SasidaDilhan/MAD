package com.example.newtestdatabinding.activities.company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.newtestdatabinding.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get references to the EditTexts and buttons from the layout XML file
        val companyNameEditText = findViewById<TextInputEditText>(R.id.reg_Cname)
        val emailEditText = findViewById<TextInputEditText>(R.id.reg_email)
        val phoneNumberEditText = findViewById<TextInputEditText>(R.id.reg_phone)
        val addressEditText = findViewById<TextInputEditText>(R.id.reg_address)
        val registrationNumberEditText = findViewById<TextInputEditText>(R.id.reg_regnumber)
        val passwordEditText = findViewById<TextInputEditText>(R.id.reg_password)
        val registerBtn = findViewById<Button>(R.id.btn_Register)
        val loginText = findViewById<TextView>(R.id.textViewR)

        loginText.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        // Set click listener for the "Register" button
        registerBtn.setOnClickListener {
            // Get the input values from the EditTexts
            val companyName = companyNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()
            val address = addressEditText.text.toString()
            val registrationNumber = registrationNumberEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validate the input values
            if (companyName.isBlank() || email.isBlank() || phoneNumber.isBlank() ||
                address.isBlank() || registrationNumber.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create the user in Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    // Create a User object to store in Firestore
                    val user = hashMapOf(
                        "companyName" to companyName,
                        "email" to email,
                        "phoneNumber" to phoneNumber,
                        "address" to address,
                        "registrationNumber" to registrationNumber
                    )

                    // Add the user to Firestore
                    db.collection("users")
                        .document(authResult.user!!.uid)
                        .set(user)
                        .addOnSuccessListener {
                            // Navigate to the MainActivity and clear the backstack
                            val intent = Intent(this, Login::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding user to Firestore", e)
                            Toast.makeText(this, "Registration failed. Please try again", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error creating user in Firebase Authentication", e)
                    Toast.makeText(this, "Registration failed. Please try again", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}