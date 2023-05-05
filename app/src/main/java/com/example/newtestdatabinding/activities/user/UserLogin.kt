package com.example.newtestdatabinding.activities.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.activities.company.About_Us
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserLogin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Get references to the EditTexts and buttons from the layout XML file
        val userEmail = findViewById<TextInputEditText>(R.id.user_log_email)
        val userPassword = findViewById<TextInputEditText>(R.id.user_log_password)
        val loginBtn = findViewById<Button>(R.id.btn_Login)
        val UtextReg = findViewById<TextView>(R.id.user_textViewLog)


        // Set click listener for the "Login" button
        loginBtn.setOnClickListener {
            // Get the input values from the EditTexts
            val userEmail = userEmail.text.toString()
            val userPassword = userPassword.text.toString()

            // Validate the input values
            if (userEmail.isBlank() || userPassword.isBlank()) {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Sign in the user with Firebase Authentication
            auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnSuccessListener { authResult ->
                    // Get the logged-in user's email
                    val userEmail = authResult.user?.email

                    // Query the 'users' collection for the document with the matching email address
                    val db = FirebaseFirestore.getInstance()
                    db.collection("App_users")
                        .whereEqualTo("email", userEmail)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            if (querySnapshot.documents.isNotEmpty()) {
                                // Get the user document from the query results
                                val userDoc = querySnapshot.documents[0]

                                // Navigate to the User_Profile activity and pass the user document ID
                                val intent = Intent(this, ProfileDisplay::class.java)
                                intent.putExtra("userDocId", userDoc.id)
                                intent.putExtra("email_user", userEmail)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            } else {
                                // User document not found
                                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            // Error querying the 'users' collection
                            Log.w(TAG, "Error querying the 'users' collection", e)
                            Toast.makeText(
                                this,
                                "Login failed. Please try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener { e ->
                    // Error signing in with Firebase Authentication
                    Log.w(TAG, "Error signing in with Firebase Authentication", e)
                    Toast.makeText(this, "Login failed. Please try again", Toast.LENGTH_SHORT)
                        .show()
                }
        }
        // Set click listener for the "Register" text
        UtextReg.setOnClickListener {
            // Navigate to the RegisterActivity
            val intent = Intent(this, UserRegister::class.java)
            startActivity(intent)
        }
    }
    companion object {
        private const val TAG = "UserLoginActivity"
    }

}