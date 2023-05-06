package com.example.newtestdatabinding.activities.user

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.model.Job
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class ApplyActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var jobTitle: TextView
    private lateinit var jobDescription: TextView
    private lateinit var jobExperience: TextView
    private lateinit var jobqualification: TextView
    private lateinit var email: TextView

    private lateinit var applyButton: Button
    private lateinit var chooseFileButton: Button
    private lateinit var cvUri: Uri
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_job)

        jobTitle = findViewById(R.id.apply_Title)
        jobDescription = findViewById(R.id.apply_description)
        jobExperience = findViewById(R.id.apply_jobExperience)
        email = findViewById(R.id.apply_e_mail)
        jobqualification = findViewById(R.id.apply_qualification)
        applyButton = findViewById(R.id.btn_apply)
        chooseFileButton = findViewById(R.id.btn_choose)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading CV...")

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        val jobId = intent.getStringExtra("jobPostId")
        jobTitle.text = intent.getStringExtra("jobTitle")
        jobDescription.text = intent.getStringExtra("description")
        jobExperience.text = intent.getStringExtra("experience")
        jobqualification.text = intent.getStringExtra("qualification")



        chooseFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 101)
        }
        applyButton.setOnClickListener {
            saveDetails()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null) {
            cvUri = data.data!!
            Toast.makeText(this, "File selected", Toast.LENGTH_SHORT).show()
        }
    }
    private fun saveDetails() {
        val cvRef = storage.reference.child("CVs").child(auth.currentUser!!.uid)

        progressDialog.show()

        val uploadTask = cvRef.putFile(cvUri)

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation cvRef.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result.toString()

                val jobApplication = hashMapOf(
                 //    "jobId" to jobId,

                    "userId" to auth.currentUser!!.uid,
                    "jobTitle" to jobTitle.text.toString(),
                    "email" to email.text.toString(),
                    "jobExperience" to jobExperience.text.toString(),
                    "cvUrl" to downloadUri
                )

                db.collection("JobApplications").add(jobApplication)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Job application submitted successfully", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Failed to submit job application", Toast.LENGTH_SHORT).show()
                    }
            } else {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload CV", Toast.LENGTH_SHORT).show()
            }
        }
    }


}