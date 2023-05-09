package com.example.newtestdatabinding.activities.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.activities.company.AdListActivity
import com.example.newtestdatabinding.activities.company.EditJobActivity
import com.example.newtestdatabinding.model.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UpdateUserProfile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var edtName : EditText
    private lateinit var edtEmail : EditText
    private lateinit var edtPhone : EditText
    private lateinit var edtAddress : EditText
    private lateinit var edtPassword : EditText
    private lateinit var saveButton : Button
    private lateinit var cancelBtn : Button
    private lateinit var deleteProfile:ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        edtName = findViewById(R.id.ed_name)
        edtEmail = findViewById(R.id.ed_email)
        edtPhone = findViewById(R.id.ed_phone)
        edtAddress = findViewById(R.id.ed_address)
        saveButton = findViewById(R.id.edt_profile)
        cancelBtn = findViewById(R.id.cnsl_profile)
        edtPassword = findViewById(R.id.ed_password)
        deleteProfile = findViewById(R.id.dlt_profile)
//        progressBar = findViewById(R.id.pr)


        setData()

        saveButton.setOnClickListener{

            val saveName = edtName.text.toString()
            val saveEmail = edtEmail.text.toString()
            val savePhone = edtPhone.text.toString()
            val saveAddress = edtAddress.text.toString()
            val savePassword = edtPassword.text.toString()

            val updateMap = mapOf(
                "userName" to saveName,
                "email" to saveEmail,
                "phoneNumber" to savePhone,
                "address" to saveAddress,
                "password" to savePassword
            )
            val intent = Intent(this, ProfileDisplay::class.java)
            startActivity(intent)

            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            db.collection("App_users").document(userId).update(updateMap)

            Toast.makeText(this, "Successfully Edited!!", Toast.LENGTH_SHORT).show()
        }
        cancelBtn.setOnClickListener{
            val intent = Intent(this, AddViewUser::class.java)
            startActivity(intent)
        }

        deleteProfile.setOnClickListener{

            val intent = Intent(this, UserLogin::class.java)
            startActivity(intent)

            val usrId = FirebaseAuth.getInstance().currentUser!!.uid
            db.collection("App_users").document(usrId).delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "successfully deleted!!",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Failiure in delete",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setData(){

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("App_users").document(userId)
        ref.get().addOnSuccessListener {
            if(it != null){
                val name = it.get("userName").toString()
                val email = it.get("email").toString()
                val phoneNumber = it.get("phoneNumber").toString()
                val address = it.get("address").toString()
                val passowrd = it.get("password").toString()


                edtName.setText(name)
                edtEmail.setText(email)
                edtPhone.setText(phoneNumber)
                edtAddress.setText(address)
                edtPassword.setText(passowrd)
//                edtPassword.setText(edtPassword)
            }
        }
            .addOnFailureListener{
                Toast.makeText(this, "Faild!!", Toast.LENGTH_SHORT).show()
            }
    }

}
