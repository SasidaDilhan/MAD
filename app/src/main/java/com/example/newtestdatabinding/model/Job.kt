package com.example.newtestdatabinding.model

data class Job(var id: String = "", var jobTitle: String = "", var description: String = "",
               var qualification: String = "", var experience: Int = 0, var cvUrl: String = "")
