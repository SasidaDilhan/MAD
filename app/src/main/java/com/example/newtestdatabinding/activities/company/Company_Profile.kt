package com.example.newtestdatabinding.activities.company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.newtestdatabinding.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Company_Profile : AppCompatActivity() {

    private  lateinit var  auth: FirebaseAuth
    private lateinit var  db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_profile)

        val logoutBtn = findViewById<ImageView>(R.id.btnLogout)
        val createAdd_btn = findViewById<Button>(R.id.create)
        val viewAdd_btn = findViewById<Button>(R.id.AddView)
        val profileView = findViewById<ImageView>(R.id.Com_Profile)
        val viewapplyuser = findViewById<Button>(R.id.btnAppliedUser)

     val currentUser = intent.getStringExtra("email_user")
        //Intialize firebase and fireStore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        //Log out button
        logoutBtn.setOnClickListener{
            auth.signOut()

            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        createAdd_btn.setOnClickListener{
            val intent = Intent(this, CreateAdd::class.java)
            intent.putExtra("email_user", currentUser)
            startActivity(intent)
        }

        viewAdd_btn.setOnClickListener{
            val intent = Intent(this, AdListActivity::class.java)
            intent.putExtra("email_user", currentUser)
            startActivity(intent)
        }

        profileView.setOnClickListener {
            val intent = Intent(this, CompanyProfileDisplay::class.java)
            startActivity(intent)
        }
        viewapplyuser.setOnClickListener {
            val intent = Intent(this, ApplyUser::class.java)
            startActivity(intent)
        }

    }
}