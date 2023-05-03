package com.example.newtestdatabinding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.model.Job


class JobAdapter : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    private var jobList = emptyList<Job>()

    fun setJobs(jobs: MutableList<com.example.newtestdatabinding.model.Job>) {
        jobList = jobs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_job, parent, false)
        return JobViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val currentJob = jobList[position]
        holder.txtJobTitle.text = currentJob.jobTitle
        holder.txtDescription.text = currentJob.description
        holder.txtQualification.text = currentJob.qualification
        holder.txtExperience.text = currentJob.experience.toString()
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtJobTitle: TextView = itemView.findViewById(R.id.jobTitle)
        val txtDescription: TextView = itemView.findViewById(R.id.tv_description)
        val txtQualification: TextView = itemView.findViewById(R.id.tv_qualification)
        val txtExperience: TextView = itemView.findViewById(R.id.tv_experience)
    }
}
