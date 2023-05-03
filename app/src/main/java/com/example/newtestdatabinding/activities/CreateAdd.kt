package com.example.newtestdatabinding.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.net.toFile
import com.example.newtestdatabinding.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CreateAdd : AppCompatActivity() {
    private lateinit var selectedFile: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_add)

        val cvButton = findViewById<Button>(R.id.btn_cv)
        val createAdd = findViewById<Button>(R.id.btn_create_ad)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Select CV file
        cvButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(intent, "Select CV file"), 0)
        }

        // Create ad button click listener
        createAdd.setOnClickListener {
            createAd()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val cvButton = findViewById<Button>(R.id.btn_cv)



        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            selectedFile = data?.data!!
            cvButton.text = "File Selected"
        }
    }

    private fun createAd() {

        val edt_jobTitle = findViewById<EditText>(R.id.edt_jobTitle)
        val edt_description = findViewById<EditText>(R.id.edt_description)
        val edt_qualification = findViewById<EditText>(R.id.edt_qualification)
        val edt_expe = findViewById<EditText>(R.id.edt_expe)
        // Get job details
        val jobTitle = edt_jobTitle.text.toString()
        val description = edt_description.text.toString()
        val qualification = edt_qualification.text.toString()
        val experience = edt_expe.text.toString().toInt()

        // Check if all fields are filled
        if (jobTitle.isNotEmpty() && description.isNotEmpty() && qualification.isNotEmpty() && experience > -1 && ::selectedFile.isInitialized) {
            // Upload CV file to Firebase Storage
            val storageRef = FirebaseStorage.getInstance().getReference("cvs/${selectedFile.lastPathSegment}")
            storageRef.putFile(selectedFile)
                .addOnSuccessListener { taskSnapshot ->
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Save job details and CV download URL to Firebase Firestore
                        val jobAdd = hashMapOf(
                            "jobTitle" to jobTitle,
                            "description" to description,
                            "qualification" to qualification,
                            "experience" to experience,
                            "cvUrl" to uri.toString()
                        )
                        FirebaseFirestore.getInstance().collection("adds").add(jobAdd)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Job Created Successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "Failed to create job : ${exception.message}!", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to upload CV file : ${exception.message}!", Toast.LENGTH_SHORT).show()
                }
        } else {
//                    Toast.makeText(this, "Failed to upload CV file : ${task.exception?.message}!", Toast.LENGTH_SHORT).show()
                }

    }
}