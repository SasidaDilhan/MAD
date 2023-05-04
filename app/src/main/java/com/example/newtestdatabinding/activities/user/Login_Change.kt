package com.example.newtestdatabinding.activities.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.activities.company.CreateAdd
import com.example.newtestdatabinding.activities.company.Login

class Login_Change : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_change)

        val userLoginChange = findViewById<Button>(R.id.user_login)
        val companyLoginChange = findViewById<Button>(R.id.company_login)
        val jobViews = findViewById<Button>(R.id.jobView)

        userLoginChange.setOnClickListener{
            val intent = Intent(this, UserLogin::class.java)
            startActivity(intent)
        }

        companyLoginChange.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        jobViews.setOnClickListener{
            val intent = Intent(this, UserView_adList::class.java)
            startActivity(intent)
        }

    }
}