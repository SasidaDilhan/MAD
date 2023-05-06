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

        if (jobId != null) {
            db.collection("adds").document(jobId).get().addOnSuccessListener { jobDoc ->
                if (jobDoc.exists()) {
                    val job = jobDoc.toObject(Job::class.java)

                    jobTitle.text = job?.jobTitle
                    jobDescription.text = job?.description
                    jobExperience.text = job?.experience
                    jobqualification.text = job?.qualification

                    applyButton.setOnClickListener {
                        uploadCV(jobId)
                    }
                }
            }
        }

        chooseFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 101)
        }
    }

    private fun uploadCV(jobId: String) {
        val cvRef = storage.reference.child("cv/${auth.currentUser?.uid}_${jobId}")

        progressDialog.show()

        cvRef.putFile(cvUri)
            .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation cvRef.downloadUrl
            })
            .addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    val cvUrl = task.result.toString()
                   // saveApplication(jobId, cvUrl)
                } else {
                    Toast.makeText(this, "Upload failed: ${task.exception}", Toast.LENGTH_SHORT).show()
                }
            }
    }}