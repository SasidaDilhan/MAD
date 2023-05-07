package com.example.newtestdatabinding.activities.company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.activities.company.EditJobActivity
import com.example.newtestdatabinding.activities.user.ProfileDisplay
import com.example.newtestdatabinding.model.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UpdateCompanyProfile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var edtName : EditText
    private lateinit var edtEmail : EditText
    private lateinit var edtPhone : EditText
    private lateinit var edtAddress : EditText
    private lateinit var edtPassword : EditText
    private lateinit var saveButton : Button
    private lateinit var cancelBtn : Button

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_company_profile2)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        edtName = findViewById(R.id.ed_name)
        edtEmail = findViewById(R.id.ed_email)
        edtPhone = findViewById(R.id.ed_phone)
        edtAddress = findViewById(R.id.ed_address)
        saveButton = findViewById(R.id.edt_profile)
        cancelBtn = findViewById(R.id.cnsl_profile)
        edtPassword = findViewById(R.id.ed_password)
//        progressBar = findViewById(R.id.pr)


        setData()

        saveButton.setOnClickListener{

            val saveName = edtName.text.toString()
            val saveEmail = edtEmail.text.toString()
            val savePhone = edtPhone.text.toString()
            val saveAddress = edtAddress.text.toString()
            val savePassword = edtPassword.text.toString()

            val updateMap = mapOf(
                "companyName" to saveName,
                "email" to saveEmail,
                "phoneNumber" to savePhone,
                "address" to saveAddress,
                "registrationNumber" to savePassword
            )
            val intent = Intent(this, CompanyProfileDisplay::class.java)
            startActivity(intent)

            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            db.collection("users").document(userId).update(updateMap)

            Toast.makeText(this, "Successfully Edited!!", Toast.LENGTH_SHORT).show()
        }
        cancelBtn.setOnClickListener{
            val intent = Intent(this, ProfileDisplay::class.java)
            startActivity(intent)
        }
    }

    private fun setData(){

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("users").document(userId)
        ref.get().addOnSuccessListener {
            if(it != null){
                val name = it.get("companyName").toString()
                val email = it.get("email").toString()
                val phoneNumber = it.get("phoneNumber").toString()
                val address = it.get("address").toString()
                val passowrd = it.get("registrationNumber").toString()


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
