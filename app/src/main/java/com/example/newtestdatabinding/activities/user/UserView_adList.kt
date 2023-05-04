package com.example.newtestdatabinding.activities.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.adapter.JobAdapter
import com.example.newtestdatabinding.model.Job
import com.google.firebase.firestore.FirebaseFirestore

class UserView_adList : AppCompatActivity(),  JobAdapter.OnJobClickListener {
    private lateinit var jobAdapter: JobAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view_ad_list)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.user_job_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create the job adapter with this activity as the click listener
        jobAdapter = JobAdapter(this)
        recyclerView.adapter = jobAdapter

        // Load all jobs from Firestore
        loadJobsUser()


    }

    private fun loadJobsUser() {
        val db = FirebaseFirestore.getInstance()

        // Query the Firestore collection for all jobs
        db.collection("adds").get()
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
    }


    override fun onEditClick(job: Job) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClick(job: Job) {
        TODO("Not yet implemented")
    }
}