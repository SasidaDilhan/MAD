package com.example.newtestdatabinding.activities.company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.adapter.JobAdapter
import com.example.newtestdatabinding.model.Job
import com.google.firebase.firestore.FirebaseFirestore

class AdListActivity : AppCompatActivity(), JobAdapter.OnJobClickListener {

    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_list)

        // Get the email of the current user from the intent extras
        val currentUser = intent.getStringExtra("email_user")

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.job_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create the job adapter with this activity as the click listener
        jobAdapter = JobAdapter(this)
        recyclerView.adapter = jobAdapter

        // Load the jobs from Firestore
        loadJobs(currentUser)
    }

    override fun onEditClick(job: Job) {
        // Start the EditJobActivity with the job ID as an intent extra
        val intent = Intent(this, EditJobActivity::class.java)
        intent.putExtra("job_id", job.id)
        startActivity(intent)
    }

    override fun onDeleteClick(job: Job) {
        val db = FirebaseFirestore.getInstance()
        db.collection("adds").document(job.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Job post deleted successfully!", Toast.LENGTH_SHORT).show()
                val currentUser = intent.getStringExtra("email_user")
                loadJobs( currentUser )
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to delete job post: ${exception.message}!", Toast.LENGTH_SHORT).show()
            }
    }



//    override fun loadJobs() {
//        TODO("Not yet implemented")
//    }

    private fun loadJobs(userEmail: String?) {
        val db = FirebaseFirestore.getInstance()

        if (userEmail != null) {
            // Query the Firestore collection for the current user's jobs
            db.collection("adds").whereEqualTo("user", userEmail).get()
                .addOnSuccessListener { result ->
                    val jobList = mutableListOf<Job>()

                    // Convert each Firestore document to a Job object and add it to the list
                    for (document in result) {
                        val job = document.toObject(Job::class.java)
                        job.id = document.id
                        jobList.add(job)
                    }

                    // Set the job list on the adapter
                    jobAdapter.setJobs(jobList)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to load jobs: ${exception.message}!", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please sign in to view your job posts!", Toast.LENGTH_SHORT).show()
        }
    }
}
