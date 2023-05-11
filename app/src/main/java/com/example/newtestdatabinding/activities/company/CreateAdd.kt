package com.example.newtestdatabinding.activities.company


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newtestdatabinding.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class CreateAdd : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_add)

        val createAdd = findViewById<Button>(R.id.btn_create_ad)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Create ad button click listener
        createAdd.setOnClickListener {
            createAd()
        }
    }

    private fun createAd() {
        val currentUser = intent.getStringExtra("email_user")

        val edt_jobTitle = findViewById<EditText>(R.id.edt_jobTitle)
        val edt_description = findViewById<EditText>(R.id.edt_description)
        val edt_qualification = findViewById<EditText>(R.id.edt_qualification)
        val edt_expe = findViewById<EditText>(R.id.edt_expe)
        // Get job details
        val jobTitle = edt_jobTitle.text.toString()
        val description = edt_description.text.toString()
        val qualification = edt_qualification.text.toString()
        val experience = edt_expe.text.toString()

        // Check if all fields are filled
        if (currentUser != null) {
            if (jobTitle.isNotEmpty() && currentUser.isNotEmpty() && description.isNotEmpty() && qualification.isNotEmpty() && experience.isNotEmpty()) {
                // Save job details to Firebase Firestore
                val jobAdd = hashMapOf(
                    "jobTitle" to jobTitle,
                    "user" to currentUser,
                    "description" to description,
                    "qualification" to qualification,
                    "experience" to experience,
                )
                FirebaseFirestore.getInstance().collection("adds").add(jobAdd)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Job Created Successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AdListActivity::class.java)
                        intent.putExtra("email_user", currentUser)
                        startActivity(intent)
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Failed to create job : ${exception.message}!", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
