package com.example.newtestdatabinding.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.adapter.JobAdapter
import com.example.newtestdatabinding.model.Job
import com.google.firebase.firestore.FirebaseFirestore


class AdListActivity : AppCompatActivity() {

    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_list)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.job_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        jobAdapter = JobAdapter()
        recyclerView.adapter = jobAdapter

        // Load jobs from Firestore
        loadJobs()
    }

    private fun loadJobs() {
        val db = FirebaseFirestore.getInstance()
        db.collection("adds").get()
            .addOnSuccessListener { result ->
                val jobList = mutableListOf<Job>()
                for (document in result) {
                    val job = document.toObject(Job::class.java)
                    job.id = document.id
                    jobList.add(job)
                }
                jobAdapter.setJobs(jobList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load jobs : ${exception.message}!", Toast.LENGTH_SHORT).show()
            }
    }
}
