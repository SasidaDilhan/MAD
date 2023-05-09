package com.example.newtestdatabinding.activities.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.model.Job
import com.google.firebase.firestore.FirebaseFirestore

class AddViewUser : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var jobAdapter: JobAdapter
    private lateinit var profileButton:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_view_user)

        profileButton = findViewById(R.id.profile_btn)
        profileButton.setOnClickListener{
            val intent = Intent(this, ProfileDisplay::class.java)
            startActivity(intent)
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Create the job adapter and set it on the RecyclerView
        jobAdapter = JobAdapter(listOf())
        recyclerView.adapter = jobAdapter

        // Load the job posts from Firestore
        loadJobPosts()

    }

    private fun loadJobPosts() {
        val db = FirebaseFirestore.getInstance()

        // Query the Firestore collection for all job posts
        db.collection("adds").get()
            .addOnSuccessListener { result ->
                val jobList = mutableListOf<Job>()

                // Convert each Firestore document to a JobPost object and add it to the list
                for (document in result) {
                    val jobPost = document.toObject(Job::class.java)
                    jobList.add(jobPost)
                }

                // Set the job list on the adapter
                jobAdapter.setJobs(jobList)
            }
            .addOnFailureListener { exception ->
                // Handle errors loading the job posts from Firestore
                // (e.g. no internet connection, incorrect permissions, etc.)
            }
    }

    inner class JobAdapter(private var jobList: List<Job>) :
        RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.job_list_user, parent, false)
            return JobViewHolder(view)


        }

        override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
            val jobPost = jobList[position]

            // Set the job post details on the view holder's views
            holder.title.text = jobPost.jobTitle
            holder.description.text = jobPost.description
            holder.qualification.text = jobPost.qualification
            holder.experience.text = jobPost.experience

            // Set the onClickListener for the Apply button
            holder.applyButton.setOnClickListener {
                // Start the ApplyActivity with the job post ID as an intent extra
                val intent = Intent(this@AddViewUser, ApplyActivity::class.java)
                intent.putExtra("jobPostId", jobPost.id)
                intent.putExtra("jobTitle", jobPost.jobTitle)
                intent.putExtra("description", jobPost.description)
                intent.putExtra("qualification", jobPost.qualification)
                intent.putExtra("experience", jobPost.experience)
                startActivity(intent)
            }

        }

        override fun getItemCount() = jobList.size

        fun setJobs(jobList: List<Job>) {
            this.jobList = jobList
            notifyDataSetChanged()
        }

        inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.job_title)
            val description: TextView = itemView.findViewById(R.id.job_description)
            val qualification: TextView = itemView.findViewById(R.id.qualification)
            val experience : TextView = itemView.findViewById(R.id.job_experience)
            val applyButton: ImageView = itemView.findViewById(R.id.apply_button)
        }
    }

}
