package com.example.newtestdatabinding.activities.company
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.model.Job
import com.example.newtestdatabinding.model.JobApplication
import com.google.firebase.firestore.FirebaseFirestore

class ApplyUser : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_user)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView2)
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
        db.collection("JobApplications").get()
            .addOnSuccessListener { result ->
                val jobList = mutableListOf<JobApplication>()

                // Convert each Firestore document to a JobPost object and add it to the list
                for (document in result) {
                    val jobPost = document.toObject(JobApplication::class.java)
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

    inner class JobAdapter(private var jobList: List<JobApplication>) :
        RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.job_list_apply, parent, false)
            return JobViewHolder(view)
        }

        override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
            val jobPost = jobList[position]

            // Set the job post details on the view holder's views
            holder.title.text = jobPost.jobTitle
            holder.email.text = jobPost.email
            holder.experience.text = jobPost.jobExperience
            holder.cvUrl.text = jobPost.cvUrl

            // Set the onClickListener for the Apply button
//            holder.applyButton.setOnClickListener {
//                // Start the ApplyActivity with the job post ID as an intent extra
//                    val intent = Intent(this@ApplyUser, ApplyActivity::class.java)
//                intent.putExtra("jobPostId", jobPost.id)
//                intent.putExtra("jobTitle", jobPost.jobTitle)
//                intent.putExtra("description", jobPost.description)
//                intent.putExtra("qualification", jobPost.qualification)
//                intent.putExtra("experience", jobPost.experience)
//                startActivity(intent)
//            }
        }

        override fun getItemCount() = jobList.size

        fun setJobs(jobList: MutableList<JobApplication>) {
            this.jobList = jobList
            notifyDataSetChanged()
        }

        inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.job_title)
            val experience : TextView = itemView.findViewById(R.id.job_experience)
            val email : TextView = itemView.findViewById(R.id.job_email)
            val cvUrl : TextView = itemView.findViewById(R.id.job_cv)

        }
    }
}



