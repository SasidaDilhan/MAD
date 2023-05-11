package com.example.newtestdatabinding.activities.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.activities.company.EditJobActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ProfileDisplay : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var disp_name: TextView
    private lateinit var disp_email: TextView
    private lateinit var disp_phone: TextView
    private lateinit var disp_address: TextView
//    private lateinit var deleteProfile : Button
    private lateinit var editProfile:ImageButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_display)



        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        disp_name = findViewById(R.id.d_name)
        disp_email = findViewById(R.id.d_email)
        disp_phone = findViewById(R.id.d_phone)
        disp_address = findViewById(R.id.d_address)
//        deleteProfile = findViewById(R.id.dlt_profile)
        editProfile = findViewById(R.id.edt_profile)

        val usrId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("App_users").document(usrId)

        ref.get().addOnSuccessListener {
            if(it != null){
                val name = it.get("userName").toString()
                val email = it.get("email").toString()
                val phoneNumber = it.get("phoneNumber").toString()
                val address = it.get("address").toString()

                disp_name.text = name
                disp_email.text = email
                disp_phone.text = phoneNumber
                disp_address.text = address

        }
        }
            .addOnFailureListener{
                Toast.makeText(this, "Faild!!", Toast.LENGTH_SHORT).show()
            }

        editProfile.setOnClickListener{
            val intent = Intent(this, UpdateUserProfile::class.java)
            startActivity(intent)
        }

//        deleteProfile.setOnClickListener{
//
//            val intent = Intent(this, UserLogin::class.java)
//               startActivity(intent)
//
//            val usrId = FirebaseAuth.getInstance().currentUser!!.uid
//            db.collection("App_users").document(usrId).delete()
//                .addOnSuccessListener {
//                    Toast.makeText(this, "successfully deleted!!",Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener{
//                    Toast.makeText(this, "Failiure in delete",Toast.LENGTH_SHORT).show()
//                }
//        }

    }

}