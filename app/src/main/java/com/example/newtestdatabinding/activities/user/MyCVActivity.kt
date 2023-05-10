package com.example.newtestdatabinding.activities.user

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newtestdatabinding.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyCVActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private var isEditable = false

    private var documentId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_resume_view)

        // Get reference to the views in the layout
        val fullNameTextView = findViewById<TextView>(R.id.fullNameTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val phoneNumberTextView = findViewById<TextView>(R.id.phoneNumberTextView)
        val educationTextView = findViewById<TextView>(R.id.educationTextView)
        val workExperienceTextView = findViewById<TextView>(R.id.workExperienceTextView)
        val extracurricularTextView = findViewById<TextView>(R.id.extracurricularTextView)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        // Get reference to the "cv-generator" collection
        val cvCollectionRef = db.collection("cv-generator")
        val query = cvCollectionRef.whereEqualTo("email", firebaseAuth.currentUser?.email)
        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // The document ID can be retrieved using document.id
                    documentId = document.id
                    // do something with the document ID
                }
                if(documentId != null) {
                    cvCollectionRef.document(documentId!!).get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val cvData = documentSnapshot.data

                                fullNameTextView.text = cvData?.get("fullName") as? String
                                emailTextView.text = cvData?.get("email") as? String
                                phoneNumberTextView.text = cvData?.get("phoneNumber") as? String
                                educationTextView.text = cvData?.get("education") as? String
                                workExperienceTextView.text =
                                    cvData?.get("experience") as? String
                                extracurricularTextView.text =
                                    cvData?.get("extracurricular") as? String
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Create your own CV first",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        .addOnFailureListener {
                        }
                }

            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        val editButton = findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            toggleEditableFields()
        }

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            saveChanges()
        }


    }

    private fun toggleEditableFields() {
        isEditable = !isEditable

        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val fullNameTextView = findViewById<TextView>(R.id.fullNameTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val phoneNumberTextView = findViewById<TextView>(R.id.phoneNumberTextView)
        val educationTextView = findViewById<TextView>(R.id.educationTextView)
        val workExperienceTextView = findViewById<TextView>(R.id.workExperienceTextView)
        val extracurricularTextView = findViewById<TextView>(R.id.extracurricularTextView)
        val editButton = findViewById<Button>(R.id.editButton)
        val saveButton = findViewById<Button>(R.id.saveButton)

        if (isEditable) {
            saveButton.visibility = View.VISIBLE
            profileImage.setOnClickListener {
                // Code to change profile image
            }

            fullNameTextView.apply {
                isFocusable = true
                isClickable = true
                isCursorVisible = true
            }

            emailTextView.apply {
                isFocusable = true
                isClickable = true
                isCursorVisible = true
            }

            phoneNumberTextView.apply {
                isFocusable = true
                isClickable = true
                isCursorVisible = true
            }

            educationTextView.apply {
                isFocusableInTouchMode = true
                isEnabled = true
                isFocusable = true
                isClickable = true
                isCursorVisible = true
                background = getDrawable(R.drawable.edit_text_background)
            }

            workExperienceTextView.apply {
                isFocusableInTouchMode = true
                isEnabled = true
                isFocusable = true
                isClickable = true
                isCursorVisible = true
                background = getDrawable(R.drawable.edit_text_background)
            }

            extracurricularTextView.apply {
                isFocusableInTouchMode = true
                isEnabled = true
                isFocusable = true
                isClickable = true
                isCursorVisible = true
                background = getDrawable(R.drawable.edit_text_background)
            }

            editButton.text = "Cancel"
        } else {
            saveButton.visibility = View.INVISIBLE
            fullNameTextView.apply {
                isFocusableInTouchMode = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }

            emailTextView.apply {
                isFocusableInTouchMode = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }

            phoneNumberTextView.apply {
                isFocusableInTouchMode = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }

            educationTextView.apply {
                isFocusableInTouchMode = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }

            workExperienceTextView.apply {
                isFocusableInTouchMode = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }

            extracurricularTextView.apply {
                isFocusableInTouchMode = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }

            editButton.text = "Edit"
        }
    }

    private fun saveChanges() {
        isEditable = !isEditable
        //update firestore
        if (documentId != null) {
            val docRef = db.collection("cv-generator").document(documentId!!)
            val updates: MutableMap<String, Any> = HashMap()


            val fullNameTextView = findViewById<TextView>(R.id.fullNameTextView)
            val emailTextView = findViewById<TextView>(R.id.emailTextView)
            val phoneNumberTextView = findViewById<TextView>(R.id.phoneNumberTextView)
            val educationTextView = findViewById<TextView>(R.id.educationTextView)
            val workExperienceTextView = findViewById<TextView>(R.id.workExperienceTextView)
            val extracurricularTextView = findViewById<TextView>(R.id.extracurricularTextView)
            val editButton = findViewById<Button>(R.id.editButton)
            val saveButton = findViewById<Button>(R.id.saveButton)
            saveButton.visibility = View.INVISIBLE

            updates["education"] = educationTextView.text.toString()
            updates["experience"] = workExperienceTextView.text.toString()
            updates["extracurricular"] = extracurricularTextView.text.toString()

            docRef.update(updates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "CV updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(applicationContext, "Failed to update CV", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            fullNameTextView.apply {
                isFocusableInTouchMode = false
                isEditable = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }

            emailTextView.apply {
                isFocusableInTouchMode = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }

            phoneNumberTextView.apply {
                isFocusableInTouchMode = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }

            educationTextView.apply {
                isFocusableInTouchMode = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }

            workExperienceTextView.apply {
                isFocusableInTouchMode = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }

            extracurricularTextView.apply {
                isFocusableInTouchMode = false
                isFocusable = false
                isClickable = false
                isCursorVisible = false
                background = null
            }
            editButton.text = "Edit"
        }else{
            Toast.makeText(
                applicationContext,
                "No CV to update",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}