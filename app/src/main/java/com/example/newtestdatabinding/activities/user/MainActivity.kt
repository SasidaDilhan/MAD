package com.example.newtestdatabinding.activities.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.newtestdatabinding.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var toogle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val nav_view: NavigationView = findViewById(R.id.nav_id)




        toogle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT)
                    .show()
                R.id.profile -> Toast.makeText(
                    applicationContext,
                    "Clicked Profile",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.add_acc -> Toast.makeText(
                    applicationContext,
                    "Clicked Add Account",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.switch_acc -> Toast.makeText(
                    applicationContext,
                    "Clicked Switch Account",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.about -> Toast.makeText(
                    applicationContext,
                    "Clicked About US",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.login -> Toast.makeText(
                    applicationContext,
                    "Clicked LogOut",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.Share -> Toast.makeText(
                    applicationContext,
                    "Clicked Share",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.rate -> Toast.makeText(applicationContext, "Clicked Rate", Toast.LENGTH_SHORT)
                    .show()

            }
            true

        }
//        val currentUser = intent.getStringExtra("email_user")
//        // Get the email of the logged-in user from the intent
//      //  val userEmail = intent.getStringExtra("email")
//
//        // Get a reference to the NavigationView and the header view
//        val navView = findViewById<NavigationView>(R.id.drawer_layout)
//        val headerView = navView.getHeaderView(0)
//
//        // Get a reference to the TextView in the header layout and set its text to the email of the logged-in user
//        val emailTextView = headerView.findViewById<TextView>(R.id.profName)
//        emailTextView.text = currentUser

    }


        override fun onOptionsItemSelected(item: MenuItem): Boolean {

            if (toogle.onOptionsItemSelected(item)) {
                return true
            }

            return super.onOptionsItemSelected(item)
        }

}
