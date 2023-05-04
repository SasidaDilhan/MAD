package com.example.newtestdatabinding.activities.company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.activities.user.UserRegister
import com.example.newtestdatabinding.activities.user.User_Profile
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Get references to the EditTexts and buttons from the layout XML file
        val emailEditText = findViewById<TextInputEditText>(R.id.log_email)
        val passwordEditText = findViewById<TextInputEditText>(R.id.log_password)
        val loginBtn = findViewById<Button>(R.id.btn_Login)
        val textReg = findViewById<TextView>(R.id.textViewLog)


        // Set click listener for the "Login" button
        loginBtn.setOnClickListener {
            // Get the input values from the EditTexts
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validate the input values
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Sign in the user with Firebase Authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    // Navigate to the MainActivity and clear the backstack
                    val intent = Intent(this, Company_Profile::class.java)
                    intent.putExtra("email_user", email)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error signing in with Firebase Authentication", e)
                    Toast.makeText(this, "Login failed. Please try again", Toast.LENGTH_SHORT).show()
                }
        }

        // Set click listener for the "Register" text
        textReg.setOnClickListener {
            // Navigate to the RegisterActivity
            val intent = Intent(this, UserRegister::class.java)
            startActivity(intent)
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }

}