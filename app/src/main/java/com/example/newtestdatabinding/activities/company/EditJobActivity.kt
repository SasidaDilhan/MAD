package com.example.newtestdatabinding.activities.company

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.model.Job
import com.google.firebase.firestore.FirebaseFirestore

class EditJobActivity : AppCompatActivity() {

    private lateinit var jobId: String
    private lateinit var jobTitleEditText: EditText
    private lateinit var jobDescriptionEditText: EditText
    private lateinit var experenceEditText : EditText
    private lateinit var qualificationEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_job)

        // Get the job ID from the intent
        jobId = intent.getStringExtra("job_id") ?: ""

        // Initialize the EditText fields
        jobTitleEditText = findViewById(R.id.title_edit_text)
        jobDescriptionEditText = findViewById(R.id.description_edit_text)
        qualificationEditText = findViewById(R.id.qualification_edit_text)
        experenceEditText = findViewById(R.id.experience_edit_text)

        // Load the job data
        loadJobData()
        // Set up the Delete button click listener

        // Set up the Save button click listener
        val saveButton: Button = findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            val jobTitle = jobTitleEditText.text.toString().trim()
            val jobDescription = jobDescriptionEditText.text.toString().trim()
            val qualification = qualificationEditText.text.toString().trim()
            val experience = experenceEditText.text.toString().trim()
            if (jobTitle.isNotEmpty() && jobDescription.isNotEmpty() && qualification.isNotEmpty() && experience.isNotEmpty()) {
                saveJobData(jobTitle, jobDescription, qualification, experience)
            } else {
                Toast.makeText(this, "Please enter a job title and description!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveJobData(jobTitle: String, jobDescription: String, qualification : String, experience : String) {
        val db = FirebaseFirestore.getInstance()
        //val currentUser = FirebaseAuth.getInstance().currentUser?.email

        // Check if the user's post ID is not empty
        if (jobId.isNotEmpty()) {
            // Update the existing job post
            db.collection("adds").document(jobId)
                .update("title", jobTitle, "description", jobDescription, "qulification", qualification, "experience", experience)
                .addOnSuccessListener {
                    Toast.makeText(this, "Job post updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to update job post: ${exception.message}!", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Add a new job post
            val job = Job(jobTitle, jobDescription)
            db.collection("adds")
                .add(job)
                .addOnSuccessListener {
                    Toast.makeText(this, "Job post added successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to add job post: ${exception.message}!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun loadJobData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("adds")
            .document(jobId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val job = document.toObject(Job::class.java)
                    jobTitleEditText.setText(job?.jobTitle)
                    jobDescriptionEditText.setText(job?.description)
                    experenceEditText.setText(job?.experience)
                    qualificationEditText.setText(job?.qualification)

                } else {
                    Toast.makeText(this, "Job post not found!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load job post: ${exception.message}!", Toast.LENGTH_SHORT).show()
            }
    }


}
