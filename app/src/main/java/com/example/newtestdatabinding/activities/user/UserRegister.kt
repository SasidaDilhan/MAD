package com.example.newtestdatabinding.activities.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.activities.company.Login
import com.example.newtestdatabinding.activities.user.UserRegister
import com.example.newtestdatabinding.activities.user.UserLogin
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserRegister : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get references to the EditTexts and buttons from the layout XML file
        val userName = findViewById<TextInputEditText>(R.id.user_name)
        val userEmail = findViewById<TextInputEditText>(R.id.user_email)
        val userPhoneNumber = findViewById<TextInputEditText>(R.id.user_phone)
        val userAddress = findViewById<TextInputEditText>(R.id.user_address)
        val userPassword = findViewById<TextInputEditText>(R.id.user_password)
        val registerBtn = findViewById<Button>(R.id.btn_Register)
        val loginText = findViewById<TextView>(R.id.textViewR)

        loginText.setOnClickListener{
            val intent = Intent(this, UserLogin::class.java)
            startActivity(intent)
        }
        // Set click listener for the "Register" button
        registerBtn.setOnClickListener {
            // Get the input values from the EditTexts
            val companyName = userName.text.toString()
            val email = userEmail.text.toString()
            val phoneNumber = userPhoneNumber.text.toString()
            val address = userAddress.text.toString()
            val password = userPassword.text.toString()

            // Validate the input values
            if (companyName.isBlank() || email.isBlank() || phoneNumber.isBlank() ||
                address.isBlank() ||  password.isBlank()) {
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
                        "address" to address
                    )
                    // Add the user to Firestore
                    db.collection("App_users")
                        .document(authResult.user!!.uid)
                        .set(user)
                        .addOnSuccessListener {
                            // Navigate to the MainActivity and clear the backstack
                            val intent = Intent(this, UserLogin::class.java)
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
        private const val TAG = "UserRegisterActivity"
    }
}