package com.example.newtestdatabinding.activities.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.newtestdatabinding.R

class Contact_Us : AppCompatActivity() {
    private lateinit var homeButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        homeButton = findViewById(R.id.home_btn)

        homeButton.setOnClickListener {
            val intent = Intent(this, Display_profile::class.java)
            startActivity(intent)
        }
    }
}