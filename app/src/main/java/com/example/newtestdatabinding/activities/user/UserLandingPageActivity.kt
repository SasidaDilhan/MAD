package com.example.newtestdatabinding.activities.user

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.newtestdatabinding.R

class UserLandingPageActivity : AppCompatActivity() {

    private lateinit var ViewResumeTile: CardView
    private lateinit var ViewProfileTile: CardView
    private lateinit var ViewNotificationsTile: CardView
    private lateinit var ViewJobMarketTile: CardView

    fun ViewResume(view: View) {}
    fun ViewProfile(view: View) {}
    fun ViewNotifications(view: View) {}
    fun ViewJobMarket(view: View) {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_landing_page)


        ViewJobMarketTile = findViewById(R.id.card1)
        ViewResumeTile = findViewById(R.id.card2)
        ViewProfileTile = findViewById(R.id.card3)
        ViewNotificationsTile = findViewById(R.id.card4)

        ViewJobMarketTile.setOnClickListener {

            val intent = Intent(this, MyCVActivity::class.java)
            startActivity(intent)
        }

        ViewResumeTile.setOnClickListener {

            val intent = Intent(this, ResumeDashboardActivity::class.java)
            startActivity(intent)
        }

        ViewProfileTile.setOnClickListener {
            val intent = Intent(this, AddViewUser::class.java)
            startActivity(intent)
//
        }

        ViewNotificationsTile.setOnClickListener {

//            val intent = Intent(this, AppliedJobsActivity::class.java)
//            startActivity(intent)
        }
    }


}