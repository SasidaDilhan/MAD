package com.example.newtestdatabinding.activities.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.databinding.ActivityMainBinding
import com.example.newtestdatabinding.model.Job
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class ResumeFormActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore



    private lateinit var firstNameEditText: TextInputEditText
    private lateinit var lastNameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var educationEditText: TextInputEditText
    private lateinit var experienceEditText: TextInputEditText
    private lateinit var extracurricularEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cv_form_view)

        db = FirebaseFirestore.getInstance()

        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        educationEditText = findViewById(R.id.educationEditText)
        experienceEditText = findViewById(R.id.experienceEditText)
        extracurricularEditText = findViewById(R.id.extracurricularEditText)



        findViewById<Button>(R.id.submitButton).setOnClickListener {
            if (isValidForm()) {

                val data = hashMapOf(
                    "fullName" to firstNameEditText.text.toString()+ " " + lastNameEditText.text.toString(),
                    "email" to emailEditText.text.toString(),
                    "phoneNumber" to phoneEditText.text.toString(),
                    "education" to educationEditText.text.toString(),
                    "experience" to experienceEditText.text.toString(),
                    "extracurricular" to extracurricularEditText.text.toString()
                )

                val phoneNumber = phoneEditText.text.toString()
                db.collection("cv-generator")
                    .document(phoneNumber)
                    .set(data)
                    .addOnSuccessListener {
                        clearForm()
                        Toast.makeText(applicationContext, "CV data saved successfully", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener {
                            e ->
                        Toast.makeText(applicationContext, "Error saving CV data: $e", Toast.LENGTH_SHORT).show()
                    }


            }


        }
    }


    private fun isValidForm(): Boolean {
        var isValid = true

        // Validate first name
        if (firstNameEditText.text.toString().isEmpty()) {
            firstNameEditText.error = getString(R.string.required)
            isValid = false
        }

        // Validate last name
        if (lastNameEditText.text.toString().isEmpty()) {
            lastNameEditText.error = getString(R.string.required)
            isValid = false
        }

        // Validate email address
        val email = emailEditText.text.toString()
        if (email.isEmpty()) {
            emailEditText.error = getString(R.string.required)
            isValid = false
        } else if (!isValidEmail(email)) {
            emailEditText.error = getString(R.string.invalid_email)
            isValid = false
        }

        // Validate phone number
        val phone = phoneEditText.text.toString()
        if (phone.isEmpty()) {
            phoneEditText.error = getString(R.string.required)
            isValid = false
        } else if (!isValidPhoneNumber(phone)) {
            phoneEditText.error = getString(R.string.invalid_phone)
            isValid = false
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    private fun clearForm() {
        findViewById<EditText>(R.id.firstNameEditText).text.clear()
        findViewById<EditText>(R.id.lastNameEditText).text.clear()
        findViewById<EditText>(R.id.emailEditText).text.clear()
        findViewById<EditText>(R.id.phoneEditText).text.clear()
        findViewById<EditText>(R.id.educationEditText).text.clear()
        findViewById<EditText>(R.id.experienceEditText).text.clear()
        findViewById<EditText>(R.id.extracurricularEditText).text.clear()
    }

}





